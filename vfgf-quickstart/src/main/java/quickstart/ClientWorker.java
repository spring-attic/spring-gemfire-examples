package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.Region;

/**
 * In this example of client/server caching, the server listens on a port for client requests and updates. The client
 * forwards all data requests it cannot fulfill to the server and is configured to update the server as well. Please
 * refer to the quickstart guide for instructions on how to run this example.
 *
 * @author GemStone Systems, Inc.
 *
 * @since 5.8
 */
public class ClientWorker {

	public static final String EXAMPLE_REGION_NAME = "exampleRegion";

	public static void main(String[] args) throws Exception {

		System.out.println("Connecting to the distributed system and creating the cache.");
		// Create the cache which causes the cache-xml-file to be parsed
		ClientCache cache = new ClientCacheFactory()
                  .set("name", "ClientWorker")
                  .set("cache-xml-file", "xml/Client.xml")
                  .create();

		// Get the exampleRegion
		Region<String, String> exampleRegion = cache.getRegion(EXAMPLE_REGION_NAME);
		System.out.println("Example region \"" + exampleRegion.getFullPath() + "\" created in cache.");
		System.out.println();
		System.out.println("Getting three values from the cache server.");
		System.out.println("This will cause the server's loader to run, which will add the values");
		System.out.println("to the server cache and return them to me. The values will also be");
		System.out.println("forwarded to any other client that has subscribed to the region.");

		// Get three values from the cache
		for (int count = 0; count < 3; count++) {
			String key = "key" + count;
			System.out.println("Getting key " + key);
			exampleRegion.get(key);
		}

		System.out.println("Note the other client's region listener in response to these gets.");
		System.out.println("Press Enter to continue.");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		bufferedReader.readLine();

		System.out.println("Changing the data in my cache - all destroys and updates are forwarded");
		System.out.println("through the server to other clients. Invalidations are not forwarded.");

		// Update one value in the cache
		System.out.println("Putting new value for key0");
		exampleRegion.put("key0", "ClientValue0");

		// Invalidate one entry in the cache
		System.out.println("Invalidating key1");
		exampleRegion.invalidate("key1");

		// Destroy one entry in the cache
		System.out.println("Destroying key2");
		exampleRegion.destroy("key2");

		// Close the cache and disconnect from GemFire distributed system
		System.out.println("Closing the cache and disconnecting.");
		cache.close();

		System.out.println("In the other session, please hit Enter in the Consumer client");
		System.out.println("and then stop the cacheserver with 'cacheserver stop'.");
	}
}
