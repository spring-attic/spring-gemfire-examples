package cacheOverflow;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;

/**
 * A worker thread that places a given number of 8K chunks of data
 * (<code>long</code> arrays) into a GemFire {@linkplain Region
 * region}.
 *
 * @author GemStone Systems, Inc.
 *
 * @since 3.2
 */
public class Worker implements Runnable {

  /** The id of this worker thread (prepended to the entry keys) */
  private int id;

  /** The region on which this worker operates */
  private Region<String, long[]> region;

  /** Number of arrays (8K chunks) that are placed into the region */
  private long arrayCount;

  /** Statistics about the work done by this worker */
  private WorkerStats stats;

  /** Do we validate the contents of the Region? */
  private boolean validate;

  ///////////////////////  Constructors  ///////////////////////

  /**
   * Creates a new <code>Worker</code> with the given id that places a
   * given number of 8K chunks of data in the given region.
   *
   * @param validate
   *        Do we validate the contents of the region instead of
   *        populating it?
   */
  Worker(int id, Region<String, long[]> region, long arrayCount, boolean validate) {
    this.id = id;
    this.region = region;
    this.arrayCount = arrayCount;
    this.validate = validate;

    this.stats =
      new WorkerStats(id, CacheFactory.getAnyInstance().getDistributedSystem());
  }

  /**
   * Places a 8K arrays of <code>long</code> into a GemFire cache
   * region and updates statistics accordingly.
   */
  public void run() {
    for (long i = 1; i <= this.arrayCount; i++) {
      if (i % 1000 == 0) {
        System.out.println("Worker " + this.id + " has " +
                           (validate ? "validated" : "put") +
                           " " + i + " 8K chunks");
      }

      String key = this.id + "-" + i;
      long[] array;
      try {
        if (!validate) {
          array = new long[1024];
          array[0] = i;
          region.put(key, array);
          stats.incBytesAdded(8 * array.length);

        } else {
          String error = null;

          Object value = region.get(key);
          if (value == null) {
            error = "Null value for key \"" + key + "\"";

          } else if (!(value instanceof long[])) {
            error = "Value for \"" + key + "\" is a " +
              value.getClass().getName();

          } else {
            array = (long[]) value;
            if (array[0] != i) {
              error = "Expected " + i + ", got " + array[0];
            }
          }

          if (error != null) {
            throw new IllegalStateException(error);
          }
        }

      } catch (Exception ex) {
        CacheFactory.getAnyInstance().getLogger().severe("While putting into the region", ex);
      }
    }
  }

}
