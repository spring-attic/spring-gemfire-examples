package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * This example shows basic Region operations on a partitioned region, which has
 * data partitions on two VMs. The region is configured to create one extra copy
 * of each data entry. The copies are placed on different VMs, so when one VM
 * shuts down, no data is lost. Operations proceed normally on the other VM.
 * Please refer to the quickstart guide for instructions on how to run this
 * example.
 * 
 * @author GemStone Systems, Inc.
 */
@Component
public class PartitionedRegionVM1 {

	// Represents region name for the partitioned region
	// inject the region
	@Resource(name = "PartitionedRegion")
	private Region<String, String> pr;

	public void execute() throws Exception {
		System.out
				.println("\nThis example shows the operation of a partitioned region on two VMs.");

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));

		System.out.println("Please start VM2.");
		bufferedReader.readLine();

		// Create three entries in the partitioned region
		final int endCount = 3;
		System.out.println("Putting " + endCount + " entries into "
				+ pr.getName() + ". . .");
		for (int count = 0; count < endCount; count++) {
			String key = "key" + count;
			String value = "value" + count;
			System.out.println("\n     Putting entry: " + key + ", " + value);
			pr.put(key, value);
		}

		System.out.println("\nPlease press Enter in VM2.");
		bufferedReader.readLine();
		// destroy and invalidate keys which are present in the partitioned
		// region
		System.out.println("Destroying and invalidating entries in "
				+ pr.getName() + ". . .");
		final String destroyKey = "key0";
		System.out.println("\n     Destroying " + destroyKey);
		pr.destroy(destroyKey);
		final String invalidateKey = "key1";
		System.out.println("\n     Invalidating " + invalidateKey);
		pr.invalidate(invalidateKey);

		System.out.println("\nPlease press Enter in VM2 again.");
		bufferedReader.readLine();
		System.out.println("Getting " + endCount + " entries from "
				+ pr.getName() + " after VM2 is closed. . .");
		for (int count = 0; count < endCount; count++) {
			String key = "key" + count;
			System.out
					.println("\n     Getting key " + key + ": " + pr.get(key));
		}

	}
}
