package quickstart;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;

/**
 * This example shows a producer/consumer system with the consumer configured 
 * as a replicate of the producer (push model, complete data replication). Please
 * refer to the quickstart guide for instructions on how to run this example. 
 * 
 * @author GemStone Systems, Inc.
 * 
 * @since 4.1.1
 */
public class PushProducer {

  public static void main(String[] args) throws Exception {
    System.out.println("\nConnecting to the distributed system and creating the cache.");
    
    // Create the cache which causes the cache-xml-file to be parsed
    Cache cache = new CacheFactory()
      .set("name", "PushProducer")
      .set("cache-xml-file", "xml/PushProducer.xml")
      .create();

    // Get the exampleRegion
    Region<String,String> exampleRegion = cache.getRegion("exampleRegion");
    System.out.println("Example region, " + exampleRegion.getFullPath() + ", created in cache. ");

    // Create 5 entries and then update those entries
    for (int iter = 0; iter < 2; iter++) {
      for (int count = 0; count < 5; count++) {
        String key = "key" + count;
        String value =  "value" + (count + 100*iter);
        System.out.println("Putting entry: " + key + ", " + value);
        exampleRegion.put(key, value);
      }
    }
    
    // Close the cache and disconnect from GemFire distributed system
    System.out.println("\nClosing the cache and disconnecting.");
    cache.close();
    
    System.out.println("\nPlease press Enter in the PushConsumer.");
  }
}

