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
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Order;

import com.gemstone.gemfire.cache.Region;

public class Client {
	private static Log log = LogFactory.getLog(Client.class);
	private static boolean partitionByCountry;
	@SuppressWarnings("unchecked")
	
	public static void main(String args[]) throws IOException {
		 if (args.length >= 1 && args[0].equalsIgnoreCase("partitionByCountry")) {
				log.debug("partitioning by country");
				partitionByCountry = true;
		}
		 
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("client/cache-config.xml");
		Region<OrderKey,Order> region = context.getBean(Region.class);

		//Create some orders
		Random rand = new Random(new Date().getTime()); 
		for (long orderId = 1; orderId <= 100; orderId++) {
			Address shipTo = new Address("Some Street","Some City",(orderId%3 == 0)?"UK":"US"); 
			Order order = new Order(orderId, (new Long(rand.nextInt(100)+1)),shipTo);
			OrderKey orderKey = getOrderKey(orderId,shipTo.getCountry());
			region.put(orderKey, order);
		}
	}
	
	private static OrderKey getOrderKey(Long id, String countryCode) {
		if (partitionByCountry) {
			return new PartitionedOrderKey(id, countryCode);
		}
		return new OrderKey(id, countryCode);
	}
}
