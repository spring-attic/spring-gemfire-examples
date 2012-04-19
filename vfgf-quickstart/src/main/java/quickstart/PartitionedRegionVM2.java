package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;

/**
 * This example shows basic Region operations on a partitioned region, which has 
 * data partitions on two VMs. The region is configured to create one extra copy 
 * of each data entry. The copies are placed on different VMs, so when one VM shuts 
 * down, no data is lost. Operations proceed normally on the other VM.  Please 
 * refer to the quickstart guide for instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 */

public class PartitionedRegionVM2
{

  public static final String regionName = "PartitionedRegion";

  public static void main(String[] args) throws Exception
  {

    System.out
        .println("\nConnecting to the distributed system and creating the cache.");

    // Create the cache which causes the cache-xml-file to be parsed
    Cache cache = new CacheFactory()
      .set("name", "PartitionedRegion")
      .set("cache-xml-file", "xml/PartitionedRegion.xml")
      .create();
    System.out.println("\nExample region, " + regionName
        + ", created in cache.");

    Region<String,String> pr = cache.getRegion(regionName);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Please press Enter in VM1.");
    bufferedReader.readLine();

    // Get three entries from the partitioned region
    final int endCount = 3;
    System.out.println("Getting " + endCount + " entries from " + regionName  + ". . .");
    for (int count = 0; count < endCount; count++) {
      String key = "key" + count;
      System.out.println("\n     Getting key " + key + ": " + pr.get(key));
    }

    System.out.println("\nPlease press Enter in VM1 again.");
    bufferedReader.readLine();

    // Get three entries from the partitioned region after destroy and invalidate
    System.out.println("Getting the same entries from " + regionName
        + " after destroy and invalidate. . .");
    for (int count = 0; count < endCount; count++) {
      String key = "key" + count;
      System.out.println("\n     Getting key " + key + ": " + pr.get(key));
    }

    System.out.println("\nAfter VM2 is closed, please press Enter in VM1 to read the values.");
    // Close the cache and disconnect from GemFire distributed system
    System.out.println("\nClosing the cache and disconnecting.");
    cache.close();
  }
}
