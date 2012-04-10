package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class PushConsumer {

  public static void main(String[] args) throws Exception {
    System.out.println("\nThis example shows how a distributed region works with replication enabled. I'll create a replicate region, then the producer will create the same region and put entries into it. Because my region is a replicate, all of the producer's puts are automatically pushed into my region. ");
    System.out.println("\nConnecting to the distributed system and creating the cache.");
    
    // Create the cache which causes the cache-xml-file to be parsed
    Cache cache = new CacheFactory()
      .set("name", "PushConsumer")
      .set("cache-xml-file", "xml/PushConsumer.xml")
      .create();

    // Get the exampleRegion
    Region<String,String> exampleRegion = cache.getRegion("exampleRegion");
    System.out.println("Example region, " + exampleRegion.getFullPath() + ", created in cache. ");
 
    System.out.println("\nPlease start the PushProducer.\n");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();
    
    // Close the cache and disconnect from GemFire distributed system
    System.out.println("Closing the cache and disconnecting.");
    cache.close();
  }
}

