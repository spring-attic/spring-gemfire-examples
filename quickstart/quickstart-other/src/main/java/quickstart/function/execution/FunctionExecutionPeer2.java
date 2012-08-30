package quickstart.function.execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Component;

import quickstart.MultiGetFunction;
import quickstart.MyArrayListResultCollector;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

/**
 * In this example of peer-to-peer function execution, one peer sends a request
 * for function execution to another peer. FunctionExecutionPeer2 creates a region,
 * populates the region and sends a function execution request to FunctionExecutionPeer1
 * while simultaneously executing the function on its own region.
 * It collects the result from its own execution as well as from FunctionExecutionPeer1.
 * Please refer to the quickstart guide for instructions on how to run this
 * example.
 * 
 * @author GemStone Systems, Inc.
 * @since 6.0
 */
@Component
public class FunctionExecutionPeer2 {
	  @Resource(name = "gemfireTemplate2")
	  private GemfireTemplate exampleRegion;
	  private final BufferedReader stdinReader;
	
  public FunctionExecutionPeer2() {
    this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
  }

  public static void main(String[] args) throws Exception {
    new FunctionExecutionPeer2().run();
  }

  public void run() throws Exception {

    writeToStdout("Peer sending function Execution request to other peer as well as executing function on its own region");

    writeToStdout("Connecting to the distributed system and creating the cache... ");

    // Get the exampleRegion
    writeToStdout("Example region \"" + exampleRegion.getRegion().getFullPath()
        + "\" created in cache.");

    // Populate the region
    for (int i = 0; i < 20; i++) {
      exampleRegion.put("KEY_" + i, "VALUE_" + i);
    }
    writeToStdout("Example region \"" + exampleRegion.getRegion().getFullPath()
        + "\" is populated.");
    
    writeToStdout("Press Enter to continue.");
    stdinReader.readLine();

    writeToStdout("Executing Function : MultiGetFunction on region \""
        + exampleRegion.getRegion().getFullPath()
        + "\" with filter size " + 3 + " and with MyArrayListResultCollector.");
    MultiGetFunction function = new MultiGetFunction();
    FunctionService.registerFunction(function);
    
    writeToStdout("Press Enter to continue.");
    stdinReader.readLine();
    
    Set<String> keysForGet = new HashSet<String>();
    keysForGet.add("KEY_4");
    keysForGet.add("KEY_9");
    keysForGet.add("KEY_7");
    Execution execution = FunctionService.onRegion(exampleRegion.getRegion()).withFilter(
        keysForGet).withArgs(Boolean.TRUE).withCollector(
        new MyArrayListResultCollector());
    ResultCollector rc = execution.execute(function);

    writeToStdout("Function executed successfully. Now getting the result");
    
    List result = (List)rc.getResult();
    writeToStdout("Got result with size " + result.size() + ".");
    writeToStdout("Press Enter to continue.");
    stdinReader.readLine();
    
    System.out.println("Closing the cache and disconnecting.");
  }

  private static void writeToStdout(String msg) {
    System.out.println(msg);
  }

}
