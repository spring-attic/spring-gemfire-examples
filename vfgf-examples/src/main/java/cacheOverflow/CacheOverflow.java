package cacheOverflow;

import static com.gemstone.gemfire.cache.RegionShortcut.LOCAL;
import static com.gemstone.gemfire.cache.RegionShortcut.LOCAL_PERSISTENT;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheException;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.DiskStoreFactory;
import com.gemstone.gemfire.cache.EvictionAction;
import com.gemstone.gemfire.cache.EvictionAttributes;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionFactory;

/**
 * An example program that demonstrates the "overflow" feature of
 * GemFire cache {@linkplain Region regions}.  It places a given
 * number of 8 kilobyte arrays of <code>long</code> into a region.  In
 * addition to displaying its progress, the program also maintains
 * {@linkplain WorkerStats statistics} about the amount of work it has
 * done.
 *
 * @author GemStone Systems, Inc.
 *
 * @since 3.2
 */
@Component
public class CacheOverflow {
  private static final PrintStream err = System.err;
  
  /**
   * Prints usage information about this program
   */
  private void usage(String s) {
    err.println("\n** " + s + "\n");
    err.println("usage: java cacheOverflow.CacheOverflow [options] args");

    err.println("\nWhere [options] are");
    err.println("  -synchronous       Writes to disk are synchronous");
    err.println("  -backup            Backup the region data on disk (data will be written");
    err.println("                     to disk as soon as it is added to the region). Restarting");
    err.println("                     the VM with the -backup flag will recover it's contents.");
    err.println("  -validate          Instead of populating a region " +
                "validates its contents.");
    err.println("  -disableCompaction Save the old backup files instead of compacting them");
    err.println("\nWhere args are");
    err.println("  threads            Number of threads adding to region");
    err.println("  arrays             Number of 8 kilobyte arrays added by " +
    "each thread");
    err.println("  overflowThreshold  Number of megabytes of region " +
    "data that can reside");
    err.println("                     in the VM before overflowing " +
    "to disk");
    err.println("  maxOplogSize       Number of megabytes of region data that can be written");
    err.println("                     to a backup file before rolling over to a new file");
    err.println("  dir+               One or more directories in which " +
                "to write region data"); 

    err.println("\n");

    System.exit(1);
  }

  /**
   * Parses the command line and launches one or more threads that
   * place data into a region.
   */
  public void execute(String[] args) {
    int threadCount = -1;
    long arrays = -1;
    long overflowThreshold = -1;
    int maxOplogSize = -1;
    Collection<File> dirs = new ArrayList<File>();
    boolean synchronous = false;
    boolean validate = false;
    boolean disableCompaction = false;
    boolean backup = false;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-synchronous")) {
        synchronous = true;

      } else if (args[i].equals("-backup")) {
        backup = true;
      } else if (args[i].equals("-validate")) {
        validate = true;
        
      } else if (args[i].equals("-disableCompaction")) {
        disableCompaction = true;
        
      } else if (args[i].startsWith("-")) {
        usage("Unknown option: " + args[i]);

      } else if (threadCount == -1) {
        try {
          threadCount = Integer.parseInt(args[i]);

        } catch (NumberFormatException ex) {
          usage("Malformed thread count: " + args[i]);
        }

      } else if (arrays == -1) {
        try {
          arrays = Long.parseLong(args[i]);

        } catch (NumberFormatException ex) {
          usage("Malformed number of arrays: " + args[i]);
        }

      } else if (overflowThreshold == -1) {
        try {
          overflowThreshold = Long.parseLong(args[i]);

        } catch (NumberFormatException ex) {
          usage("Malformed overflow threshold: " + args[i]);
        }
        
      } else if (maxOplogSize == -1) {
        try {
          maxOplogSize = Integer.parseInt(args[i]);

        } catch (NumberFormatException ex) {
          usage("Malformed maxOplogSize: " + args[i]);
        }
        
      } else {
        dirs.add(new File(args[i]));
      }
    }

    if (threadCount == -1) {
      usage("Missing number of threads");

    } else if (arrays == -1) {
      usage("Missing number of arrays");

    } else if (overflowThreshold == -1) {
      usage("Missing overflow threshold");

    } else if (maxOplogSize == -1) {
      usage("Missing maxOplogSize");
      
    } else if (dirs.isEmpty()) {
      usage("Missing directories");
    }

    // Create the region
    Properties p = new Properties();
      // With the port set to 0, and with default settings for locators,
      // this VM runs as a "standalone" process.
    p.setProperty("mcast-port", "0"); 
    p.setProperty("statistic-sampling-enabled", "true");
    p.setProperty("statistic-archive-file", "statArchive.gfs");
    Region<String, long[]> region;
    Cache cache;
    try {
      cache = new CacheFactory(p).create();
      
      DiskStoreFactory dsf = cache.createDiskStoreFactory();
      dsf.setDiskDirs(dirs.toArray(new File[dirs.size()]));
      dsf.setTimeInterval(1000);
      //Setting the queue size to 0 indicates that we will allow
      //an unlimited number of entries to be queued to be written to disk
      dsf.setQueueSize(0);
      dsf.setAutoCompact(!disableCompaction);
      dsf.setMaxOplogSize(maxOplogSize);
      dsf.create(DiskStoreFactory.DEFAULT_DISK_STORE_NAME);
      
      RegionFactory<String, long[]> regionFactory =  null;
      if(backup) {
        regionFactory = cache.createRegionFactory(LOCAL_PERSISTENT);
      } else {
        regionFactory = cache.createRegionFactory(LOCAL);
      }
      
      regionFactory.setDiskSynchronous(synchronous);
      regionFactory.setEvictionAttributes(EvictionAttributes
          .createLRUMemoryAttributes((int) overflowThreshold, null /* sizer */,
                EvictionAction.OVERFLOW_TO_DISK));
      region = regionFactory.create("CacheOverflow");
    } catch (CacheException ex) {
      err.println("While creating the Cache and Region:");
      ex.printStackTrace(err);
      System.exit(1);
      return;
    }
    
    Thread[] threads = new Thread[threadCount];

    for (int i = 0; i < threadCount; i++) {
      threads[i] =
        new Thread(new Worker(i, region, arrays, validate),
                   "Worker " + i);
      threads[i].start();
    }

    for (int i = 0; i < threadCount; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        cache.getLogger().severe("Interrupted while joining on " + threads[i], ex);
        break; // interrupted, just get out
      }
    }
    // Waiting for vsd stats file to be written.
    try {
      Thread.sleep(1500);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
