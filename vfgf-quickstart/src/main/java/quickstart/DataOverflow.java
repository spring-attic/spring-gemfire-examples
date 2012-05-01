package quickstart;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * This example shows cached data overflow to disk. Overflow is used to keep a
 * region size in check without completely destroying the data. It is specified
 * as the eviction action of a Least Recently Used (LRU) eviction controller
 * installed on the exampleRegion. Please refer to the quickstart guide for
 * instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * @since 4.1.1
 */
@Component
public class DataOverflow {

	private final BufferedReader inputReader;

	private String overflowDirString;

	@Resource(name = "exampleRegion")
	private Region<String, byte[]> exampleRegion;

	public void run() throws Exception {

		createOverflowDirectory();
		initialize();
		causeOverflow();
		destroyOverflowRegion();

		System.exit(0);
	}

	public DataOverflow() {
		this.inputReader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void createOverflowDirectory() {
		System.out
				.println("This example uses disk to extend a region's capacity. The region is");
		System.out
				.println("configured with an eviction controller that overflows data to disk when");
		System.out.println("the region reaches a specified capacity.");

	}

	private void initialize() throws IOException {
		@SuppressWarnings("deprecation")
		File[] overflowDirs = exampleRegion.getAttributes().getDiskDirs();
		overflowDirString = "";
		for (int i = 0; i < overflowDirs.length; i++) {
			if (i > 0) {
				overflowDirString += ", ";
			}
			overflowDirString += overflowDirs[i];
		}
	}

	private void causeOverflow() throws IOException {

		System.out.println();
		System.out
				.println("Putting 150 cache entries of 10 kilobytes each into the cache. When the");
		System.out
				.println("configured limit of 1 megabyte capacity is reached, the data will overflow");
		System.out.println("to files in " + overflowDirString + ".");

		for (long i = 0; i < 150; i++) {
			byte[] array = new byte[10 * 1024]; // size approximately 10 KB
			exampleRegion.put("key" + i, array);
		}

		System.out.println();
		System.out.println("Finished putting entries.");
		System.out.println();
		System.out.println("Use another shell to see the overflow files in "
				+ overflowDirString + ".");
		System.out
				.println("The disk is used to extend available memory and these files are");
		System.out.println("treated as part of the local cache.");
		System.out.println();

		pressEnterToContinue();
	}

	private void destroyOverflowRegion() throws IOException {
		System.out.println();
		System.out.println("Destroying exampleRegion...");
		exampleRegion.destroyRegion();

		System.out.println();
		System.out.println("Please look again in " + overflowDirString
				+ ". The overflow files are");
		System.out.println("deleted when the region is destroyed.");
		System.out.println();

		pressEnterToContinue();
	}

	private void pressEnterToContinue() throws IOException {
		System.out.println("Press Enter in this shell to continue.");
		inputReader.readLine();
	}
}
