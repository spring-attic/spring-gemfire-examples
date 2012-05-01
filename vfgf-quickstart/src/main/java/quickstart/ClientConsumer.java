package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * In this example of client/server caching, the server listens on a port for
 * client requests and updates. A clientworker forwards data requests to the
 * server and sends data updates to the server.
 * 
 * This client is a consumer. It registers interest in events on the server and
 * the server sends automatic updates for the events.
 * 
 * Please refer to the quickstart guide for instructions on how to run this
 * example.
 * 
 * @author GemStone Systems, Inc.
 * 
 * @since 5.8.
 */
@Component
public class ClientConsumer {

	public static enum RegisterInterestType {
		ALL_KEYS, KEYSET, REGEX
	}

	// inject the region
	@Resource(name = "exampleRegion")
	private Region<String, ?> exampleRegion;

	public void consume(RegisterInterestType registerInterestType)
			throws Exception {

		switch (registerInterestType) {
		case ALL_KEYS:
			System.out
					.println("Asking the server to send me all data additions, updates, and destroys. ");
			exampleRegion.registerInterest("ALL_KEYS");

			break;
		case KEYSET:
			System.out
					.println("Asking the server to send me events for data with these keys: 'key0', 'key1'");
			exampleRegion.registerInterest("key0");
			exampleRegion.registerInterest("key1");
			break;
		case REGEX:
			System.out
					.println("Asking the server to register interest in keys matching this");
			System.out.println("regular expression: 'k.*2'");
			exampleRegion.registerInterestRegex("k.*2");
			break;
		default:
			// Can't happen
			throw new RuntimeException();
		}

		System.out
				.println("The data region has a listener that reports all changes to standard out.");
		System.out.println();
		System.out
				.println("Please run the ClientWorkerApp in another session. It will update the");
		System.out
				.println("cache and the server will forward the updates to me. Note the listener");
		System.out.println("output in this session.");
		System.out.println();
		System.out
				.println("When the other client finishes, hit Enter to exit this program.");

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));
		bufferedReader.readLine();

	}
}
