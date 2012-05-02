package quickstart;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * This example shows cached data eviction. Use eviction to keep a region size
 * in check when you can easily get the data again from an outside source. If
 * you have data that is hard to retrieve again, you might want to use data
 * overflow. (See the DataOverflow example.) The mechanism for deciding when and
 * what to remove from memory is the same for overflow and standard eviction.
 * Standard eviction just destroys the entry instead of copying it out to disk.
 * Both use a Least Recently Used (LRU) eviction controller to decide what to
 * remove from memory. Please refer to the quickstart guide for instructions on
 * how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * @since 5.8
 */
@Component
public class DataEviction {

	@Resource(name = "exampleRegion")
	private Region<String, String> exampleRegion;

	public void run() throws Exception {
		System.out
				.println("This example keeps the region size below 10 entries by destroying the ");
		System.out
				.println("least recently used entry when an entry addition would take the count");
		System.out.println("over 10.");
		System.out.println();
		System.out
				.println("You can set capacity limits based on entry count, absolute region size,");
		System.out.println("or region size as a percentage of available heap.");
		System.out.println();

		System.out
				.println("Putting 12 cache entries into the cache. The listener will report on");
		System.out
				.println("the puts and on any destroys done by the eviction controller.");
		for (long i = 1; i < 13; i++) {
			exampleRegion.put("key" + i, "value" + i);
			Thread.sleep(10);
		}
	}
}
