package quickstart;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gemstone.gemfire.cache.server.CacheServer;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;

/**
 * Application Function to retrieve values for multiple keys in a region
 * 
 * @author Gemstone Systems Inc
 * @since 6.0
 * 
 */
public class MultiGetFunction extends FunctionAdapter {

  public void execute(FunctionContext fc) {
    if(fc instanceof RegionFunctionContext){
    RegionFunctionContext context = (RegionFunctionContext)fc;
    Set keys = context.getFilter();
    Set keysTillSecondLast = new HashSet();
    int setSize = keys.size();
    Iterator keysIterator = keys.iterator();
    for (int i = 0; i < (setSize - 1); i++) {
      keysTillSecondLast.add(keysIterator.next());
    }
    for (Object k : keysTillSecondLast) {
      context.getResultSender().sendResult(
          (Serializable)PartitionRegionHelper.getLocalDataForContext(context)
              .get(k));
    }
    Object lastResult = keysIterator.next();
    context.getResultSender().lastResult(
        (Serializable)PartitionRegionHelper.getLocalDataForContext(context)
            .get(lastResult));
    }else {
      fc.getResultSender().lastResult(Runtime.getRuntime().freeMemory()/(1024*1024)); 
    }
  }

  public String getId() {
    return getClass().getName();
  }
}

