package helloworld;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;

/**
 * This example shows two members with replicated regions. This member puts
 * entries into the replicated region. Please refer to the quickstart guide for
 * instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * 
 * @since 6.5
 */
@Component
public class HelloWorldProducer {
	private static final Log log = LogFactory.getLog(HelloWorldConsumer.class);
	// inject the region
	@Resource(name = "exampleRegion")
	private Region<String, String> exampleRegion;

	
	public void produce() {

    System.out.println("\nConnecting to the distributed system and creating the cache.");
    
    // Create the cache which causes the cache-xml-file to be parsed
//    Cache cache = new CacheFactory()
//     .set("cache-xml-file", "xml/HelloWorld.xml")
//    .create();

    // Get the exampleRegion
 //   Region<String,String> exampleRegion = cache.getRegion("exampleRegion");
    System.out.println("Example region, " + exampleRegion.getFullPath() + ", created in cache. ");

    System.out.println("Putting entry: Hello, World");
    exampleRegion.put("Hello", "World");
    System.out.println("Putting entry: Hello, Moon!");
    exampleRegion.put("Hello", "Moon!");
    
    // Close the cache and disconnect from GemFire distributed system
    System.out.println("\nClosing the cache and disconnecting.");
//    cache.close();
    
    System.out.println("\nPlease press Enter in the HelloWorldConsumer.");
  }
}

