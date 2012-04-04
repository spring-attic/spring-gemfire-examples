package cacheOverflow;

import com.gemstone.gemfire.*;

/**
 * {@linkplain Statistics Statistics} about the work performed by a
 * {@link Worker}.
 *
 * @author GemStone Systems, Inc.
 *
 * @since 3.2
 */
public class WorkerStats {

  /** The statistic type of the <code>WorkerStats</code> */
  private static StatisticsType type;

  /** The offset of the "bytesAdded" statistic */
  private static int bytesAddedOffset;

  ///////////////////////  Instance Fields  //////////////////////

  /** The actual object that keeps track of stats */
  private final Statistics stats;

  ///////////////////////  Static Methods  ///////////////////////

  /**
   * Initializes the statistics type using the given
   * <code>StatisticsTypeFactory</code>.  Note that this method is
   * synchronized because we only want the statistics type created
   * once. 
   */
  private static synchronized void
    initializeStatsType(StatisticsTypeFactory factory) {
    
    if (type != null) {
      return;
    }

    String typeDesc = "Statistics about a Worker thread";

    String bytesAddedDesc =
      "The number of bytes added to the overflow region";

    type = factory.createType(WorkerStats.class.getName(), typeDesc,
      new StatisticDescriptor[] {
        factory.createLongCounter("bytesAdded", bytesAddedDesc,
                                  "bytes", true /* largerBetter */)
      });

    bytesAddedOffset = type.nameToDescriptor("bytesAdded").getId();
  }

  ////////////////////////  Constructors  ////////////////////////

  /**
   * Creates a new <code>WorkerStats</code> for the worker with the
   * given id and registers it with the given
   * <code>StatisticsFactory</code>.
   */
  WorkerStats(int workerId, StatisticsFactory factory) {
    initializeStatsType(factory);

    this.stats = factory.createStatistics(type, "Worker " + workerId);
  }

  //////////////////////  Instance Methods  //////////////////////

  /**
   * Increments the total number of bytes added to the region by a
   * given amount.
   */
  void incBytesAdded(long bytesAdded) {
    this.stats.incLong(bytesAddedOffset, bytesAdded);
  }

}
