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
public class PartitionedRegionVM2 {

	// Represents region name for the partitioned region
	// inject the region
	@Resource(name = "PartitionedRegion")
	private Region<String, String> pr;

	public void execute() throws Exception {

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));

		System.out.println("Please press Enter in VM1.");
		bufferedReader.readLine();

		// Get three entries from the partitioned region
		final int endCount = 3;
		System.out.println("Getting " + endCount + " entries from "
				+ pr.getName() + ". . .");
		for (int count = 0; count < endCount; count++) {
			String key = "key" + count;
			System.out
					.println("\n     Getting key " + key + ": " + pr.get(key));
		}

		System.out.println("\nPlease press Enter in VM1 again.");
		bufferedReader.readLine();

		// Get three entries from the partitioned region after destroy and
		// invalidate
		System.out.println("Getting the same entries from " + pr.getName()
				+ " after destroy and invalidate. . .");
		for (int count = 0; count < endCount; count++) {
			String key = "key" + count;
			System.out
					.println("\n     Getting key " + key + ": " + pr.get(key));
		}

		System.out
				.println("\nAfter VM2 is closed, please press Enter in VM1 to read the values.");
	}
}
