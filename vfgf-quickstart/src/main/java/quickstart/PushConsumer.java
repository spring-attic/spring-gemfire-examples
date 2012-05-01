package quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

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
public class PushConsumer {

	public void consume() throws Exception {
		System.out
				.println("\nThis example shows how a distributed region works with replication enabled. I'll create a replicate region, then the producer will create the same region and put entries into it. Because my region is a replicate, all of the producer's puts are automatically pushed into my region. ");

		System.out.println("\nPlease start the PushProducer.\n");
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));
		bufferedReader.readLine();

	}
}
