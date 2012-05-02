package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;

/**
 * <p>
 * DurableClient.java has the client which connects to the DurableServer.java.
 * </p>
 * 
 * <p>
 * Ensure that before running this class the Server is up.
 * </p>
 * 
 * <p>
 * Prerequisites: Build the QuickStart(./build.sh compile-quickstart) and set
 * the CLASSPATH to include $GEMFIRE/quickstart/classes
 * </p>
 * 
 * <p>
 * Following are the steps to test the Durable Functionality.
 * </p>
 * 
 * <ol>
 * <li>Start DurableServer - Here initializing keys key1,key2,key3,key4 with
 * values K1,K2,K3,K4</li>
 * <li>Start DurableClient</li>
 * <li>Press Enter in the Server Window to update
 * <ul>
 * <li>The values in the Server Cache are updated.</li>
 * <li>Note the updates being passed onto client for all four keys (2
 * durable(key3,key4)and 2 non-durable(key1,key2))</li> </oul></li>
 * <li>Press Enter in Client Window to see the values in client Cache.
 * <ul>
 * <li>We find all the four Keys being updated.</li>
 * <li>Program exits</li>
 * </ul>
 * </li>
 * <li>Restart the Client by running DurableClient.java</li>
 * <li>Do update on the server by pressing enter in the server window.
 * <ul>
 * <li>Note that only for key3,key4 (for which registerKeys was done as Durable)
 * update is received in client window</li>
 * </ul>
 * </li>
 * <li>Press Enter in Client to get the Values
 * <ul>
 * <li>we get values for key1,key2(non-durable) as null - for key3,key4 we get
 * the updated Values.</li>
 * </ul>
 * </li>
 * 
 * <p>
 * To stop the server, type "Exit" in server console.
 * </p>
 * 
 * @author GemStone Systems, Inc.
 */

@Component
public class DurableClient {
	@Resource(name = "exampleRegion")
	private Region<String, String> exampleRegion;

	@Resource(name = "gemfire-cache")
	private ClientCache cache;

	public void run() throws Exception {
		writeToStdout("Sending Client Ready...");
		cache.readyForEvents();

		writeToStdout();
		writeToStdout("Press Enter in the server window to do an update on the server.");
		writeToStdout("Then press Enter in the client window to continue.");
		new BufferedReader(new InputStreamReader(System.in)).readLine();

		writeToStdout();
		writeToStdout("After the update on the server, the region contains:");
		writeToStdout("key1 => " + exampleRegion.get("key1"));
		writeToStdout("key2 => " + exampleRegion.get("key2"));
		writeToStdout("key3 => " + exampleRegion.get("key3"));
		writeToStdout("key4 => " + exampleRegion.get("key4"));

		writeToStdout("Closing cache but maintaining queues");
		cache.close(true);
	}

	private static void writeToStdout() {
		System.out.println("[DurableClient]");
	}

	private static void writeToStdout(String msg) {
		System.out.print("[DurableClient] ");
		System.out.println(msg);
	}
}
