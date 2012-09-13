/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.gemfire.examples;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.NoAvailableServersException;
import com.gemstone.gemfire.cache.client.Pool;

public class Client {

	
	
	private static final int WAIT_TIME_BETWEEN_ENTRIES = 500;
	
	private static final int WAIT_TIME_BETWEEN_RETRIES = 1000;
	
	private static final int NUMBER_OF_ENTRIES = 500;
	
	private static final int MAXIMUM_RETRIES = 1;

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException,
			InterruptedException {

		System.out
				.println("Start a locator on port 10334 and two or three Servers. Press <Enter> to continue");
		System.in.read();

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"client/cache-config.xml");
		Region<Long, Customer> region = context.getBean(Region.class);

		System.out
				.println("Terminate a Server process while entries are being added every " + WAIT_TIME_BETWEEN_ENTRIES + " msec.");

		/*
		 * Slowly add entries
		 */
		
		for (long id = 1; id < NUMBER_OF_ENTRIES; id++) {
			Customer cust = new Customer(id, new EmailAddress(
					"cust@customer.com"), "A", "Customer");
			
			int attemptCounter = MAXIMUM_RETRIES;
			
			while (attemptCounter > 0) {
				try {
					region.put(id, cust);
					attemptCounter = -1;
				} catch (Exception e) {
					attemptCounter--;
					System.out.println("Got an exception "  + e.getClass().getName());
					Thread.sleep(WAIT_TIME_BETWEEN_RETRIES);
				}
			}
			
			if (attemptCounter == 0) {
				System.out.println("Region operation failed for key " + id);
			}
			
			Thread.sleep(WAIT_TIME_BETWEEN_ENTRIES);
		}

		System.exit(0);
	}
}
