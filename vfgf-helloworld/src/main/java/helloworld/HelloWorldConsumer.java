package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;

/**
 * This example shows two members with replicated regions. This member will
 * store a copy of the updates sent from the HelloWorldProducer. Please refer to
 * the quickstart guide for instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * 
 * @since 6.5
 */
@Component
public class HelloWorldConsumer {
	private static final Log log = LogFactory.getLog(HelloWorldConsumer.class);

	// inject the region
	@Resource(name = "exampleRegion")
	private Region<String, String> exampleRegion;

	@PostConstruct
	void start() {
		log.info("Member " + exampleRegion.getCache().getDistributedSystem().getDistributedMember().getId()
				+ " connecting to region [" + exampleRegion.getName() + "]");
	}

	@PreDestroy
	void stop() throws Exception {
		log.info("Member " + exampleRegion.getCache().getDistributedSystem().getDistributedMember().getId()
				+ " disconnecting from region [" + exampleRegion.getName() + "]");
	}
	
	public void consume() {
		System.out
				.println("\nThis example shows how a distributed region works with replication enabled. I'll create a replicate region, then the producer will create the same region and put entries into it. Because my region is a replicate, all of the producer's puts are automatically pushed into my region. ");
		System.out
				.println("\nConnecting to the distributed system and creating the cache.");

		System.out.println("Example region, " + exampleRegion.getFullPath()
				+ ", created in cache. ");
		
		System.out.println("\nPlease start the HelloWorldProducer.\n");
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}