package quickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Component;

/**
 * In this example of hierarchical caching, the server listens on a port for
 * client requests and updates. The client creates and executes CQ on the server
 * and waits for the events. Please refer to the quickstart guide for
 * instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * @since 5.1
 */
@Component
public class CqClient {

	@Resource(name = "exampleRegionTemplate")
	private GemfireTemplate exampleRegionTemplate;

	private final BufferedReader stdinReader;

	public CqClient() {
		this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run() throws Exception {

		System.out
				.println("This client will update the server cache and its CQ listener will");
		System.out.println("get events for any changes to the CQ result set.");
		System.out
				.println("CQ events provide the base operation (the change to the server's cache),");
		System.out
				.println("and the query operation (the change to the CQ's result set).");
		pressEnterToContinue();

		// Create a new value in the cache
		System.out
				.println("___________________________________________________________________");
		System.out
				.println("CQ looking for entries whose value is 'ClientFirstValue'...");
		System.out.println("Putting key1 with value 'ClientFirstValue'");
		System.out
				.println("This satisfies the query, so the CqListener will report a query");
		System.out.println("creation event from the server cache.");
		exampleRegionTemplate.put("key1", "ClientFirstValue");

		// Wait for the events to come through so the screen output makes sense
		Thread.sleep(2000);
		pressEnterToContinue();

		// Update value.
		System.out
				.println("___________________________________________________________________");
		System.out
				.println("CQ looking for entries whose value is 'ClientFirstValue'...");
		System.out.println("Updating key1 with value 'ClientSecondValue'");
		System.out
				.println("This removes key1 from the CQ result set, to the CQListener will");
		System.out.println("report a query destroy event.");

		exampleRegionTemplate.put("key1", "ClientSecondValue");
		Thread.sleep(2000);
		pressEnterToContinue();

		// Update value.
		System.out
				.println("___________________________________________________________________");
		System.out
				.println("CQ looking for entries whose value is 'ClientFirstValue'...");
		System.out.println("Updating key1 with value 'ClientFirstValue'");
		System.out
				.println("This adds key1 back into the CQ result set, to the CQListener will");
		System.out.println("report a query create event.");

		exampleRegionTemplate.put("key1", "ClientFirstValue");
		Thread.sleep(2000);
		pressEnterToContinue();

		// Destroy value.
		System.out
				.println("___________________________________________________________________");
		System.out
				.println("CQ looking for entries whose value is 'ClientFirstValue'...");
		System.out.println("Destroying key1...");
		System.out
				.println("This removes key1 from the result set, to the CQListener will");
		System.out.println("report a query destroy event.");

		exampleRegionTemplate.remove("key1");
		Thread.sleep(2000);
		pressEnterToContinue();

	}

	private void pressEnterToContinue() throws IOException {
		System.out.println("Press Enter to continue.");
		stdinReader.readLine();
	}
}
