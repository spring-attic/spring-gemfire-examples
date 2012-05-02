package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * DurableServer.java has the server to which the DurableClient connects. See
 * the DurableClient or the quickstart guide for run instructions. To stop the
 * server, type "Exit" in server console.
 * 
 * @author GemStone Systems, Inc.
 */
@Component
public class DurableServer {

	@Resource(name = "exampleRegion")
	Region<String, String> exampleRegion;

	private void writeToStdout(String msg) {
		System.out.print("[DurableServer] ");
		System.out.println(msg);
	}

	private void writeToStdout() {
		System.out.println("[DurableServer]");
	}

	public void run() throws Exception {
		writeToStdout("This example demonstrates durable caching. This program is a server,");
		writeToStdout("listening on a port for client requests. The client program connects and");
		writeToStdout("requests data. The client in this example is also configured to forward");
		writeToStdout("information on data destroys and updates.");

		BufferedReader stdinReader = new BufferedReader(new InputStreamReader(
				System.in));

		writeToStdout();
		writeToStdout("Please start the DurableClientApp now and press Enter.");
		stdinReader.readLine();

		writeToStdout("Initializing the cache:");
		writeToStdout("Putting key1 => value1");
		exampleRegion.put("key1", "value1");
		writeToStdout("Putting key2 => value2");
		exampleRegion.put("key2", "value2");
		writeToStdout("Putting key3 => value3");
		exampleRegion.put("key3", "value3");
		writeToStdout("Putting key4 => value4");
		exampleRegion.put("key4", "value4");

		for (;;) {
			writeToStdout();
			writeToStdout("Press Enter in the server window to update the values in the cache, or 'Exit' to shut down.");
			String input = stdinReader.readLine();
			if (input == null || input.equalsIgnoreCase("Exit")) {
				break;
			}

			writeToStdout("Before updating, the values are:");
			writeToStdout("key1 => " + exampleRegion.get("key1"));
			writeToStdout("key2 => " + exampleRegion.get("key2"));
			writeToStdout("key3 => " + exampleRegion.get("key3"));
			writeToStdout("key4 => " + exampleRegion.get("key4"));

			exampleRegion.put("key1", exampleRegion.get("key1") + "1");
			exampleRegion.put("key2", exampleRegion.get("key2") + "2");
			exampleRegion.put("key3", exampleRegion.get("key3") + "3");
			exampleRegion.put("key4", exampleRegion.get("key4") + "4");

			writeToStdout("The values have been updated in the server cache.");
			writeToStdout("Press Enter in the client window to verify the Updates.");
			writeToStdout();
			writeToStdout("After updating the values, new values in server cache are:");
			writeToStdout("key1 => " + exampleRegion.get("key1"));
			writeToStdout("key2 => " + exampleRegion.get("key2"));
			writeToStdout("key3 => " + exampleRegion.get("key3"));
			writeToStdout("key4 => " + exampleRegion.get("key4"));
		}

	}
}
