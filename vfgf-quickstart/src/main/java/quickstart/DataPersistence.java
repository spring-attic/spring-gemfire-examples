package quickstart;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.DiskStore;
import com.gemstone.gemfire.cache.Region;

/**
 * This example shows the persistence of cached data to disk. Persisted data provides a backup of the cached data and
 * can be used to initialize a data region at creation time. Please refer to the quickstart guide for instructions on
 * how to run this example.
 *
 * @author GemStone Systems, Inc.
 * @since 4.1.1
 */
@Component
public class DataPersistence {
	@Resource(name = "exampleRegion")
	private Region<String, String> exampleRegion;
	
	public void run() throws Exception {

		System.out.println("This example shows persistence of region data to disk.");

		@SuppressWarnings("deprecation")
		File[] persistDir = exampleRegion.getAttributes().getDiskDirs();
		
		
		String persistDirString = "";
		for (int i = 0; i < persistDir.length; i++) {
			if (i > 0) {
				persistDirString += ", ";
			}
			persistDirString += persistDir[i];
		}

		System.out.println();
		System.out.println("Look in " + persistDirString + " to see the files used for region ");
		System.out.println("persistence.");

		// Try to obtain the value for the "key"
		String key = "key1";
		System.out.println();
		System.out.println("Getting value for " + key);
		String value = exampleRegion.get(key);

		if (value == null) {
			System.out.println("No value found for key " + key + ". Get operation returned null.");
			// Create initial value to put...
			value = "value1";
		} else {
			System.out.println("Get returned value: " + value);
			// Increment value to update...
			value = "value" + (Integer.parseInt(value.substring(5)) + 1);
		}

		System.out.println();
		System.out.println("Putting entry: " + key + ", " + value);
		exampleRegion.put(key, value);

		System.out.println();
		System.out.println("Each time you run this program, if the disk files are available, they");
		System.out.println("will be used to initialize the region.");
	}
}
