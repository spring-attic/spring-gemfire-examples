package quickstart;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * This example shows a producer/consumer system with the consumer configured as
 * a replicate of the producer (push model, complete data replication). Please
 * refer to the quickstart guide for instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * 
 * @since 4.1.1
 */
@Component
public class PushProducer {
	// inject the region
	@Resource(name = "exampleRegion")
	private Region<String, String> exampleRegion;

	public void produce() throws Exception {

		// Create 5 entries and then update those entries
		for (int iter = 0; iter < 2; iter++) {
			for (int count = 0; count < 5; count++) {
				String key = "key" + count;
				String value = "value" + (count + 100 * iter);
				System.out.println("Putting entry: " + key + ", " + value);
				exampleRegion.put(key, value);
			}
		}

		System.out.println("\nPlease press Enter in the PushConsumer.");
	}
}
