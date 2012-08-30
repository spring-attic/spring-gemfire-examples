package quickstart.function.execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Component;

import quickstart.MultiGetFunction;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionService;

/**
 * This is the peer to which FunctionExecutionPeer2 connects for function execution.
 * This peer executes the function execution request and returns the results to
 * the requesting peer
 * 
 * @author GemStone Systems, Inc.
 * @since 6.0
 */
@Component
public class FunctionExecutionPeer1 {

  @Resource(name = "gemfireTemplate1")
  private GemfireTemplate exampleRegion;
  private final BufferedReader stdinReader;

  public FunctionExecutionPeer1() {
    this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
  }

   public void run() throws Exception {

    writeToStdout("Peer to which other peer sends request for function Execution");
    writeToStdout("Connecting to the distributed system and creating the cache... ");

    // Get the exampleRegion
    writeToStdout("Example region \"" + exampleRegion.getRegion().getFullPath()
        + "\" created in cache.");

    writeToStdout("Registering the function MultiGetFunction on Peer");
    MultiGetFunction function = new MultiGetFunction();
    FunctionService.registerFunction(function);

    writeToStdout("Please start Other Peer And Then Press Enter to continue.");
    stdinReader.readLine();
    
    System.out.println("Closing the cache and disconnecting.");
  }

  private static void writeToStdout(String msg) {
   // System.out.print("[FunctionExecutionPeer1] ");
    System.out.println(msg);
  }

}
