package org.springframework.data.gemfire.examples;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;

import com.gemstone.gemfire.cache.Region;

public class Main {
	private static Log log = LogFactory.getLog(Main.class);

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException {
		createDiskStoreDirectories();
		deleteDiskStoresIfRequested();

		log.debug("initializing application context...");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cache-config.xml");

		Region<Long, Product> productRegion = context.getBean("Product",
				Region.class);
		Region<Long, Order> orderRegion = context
				.getBean("Order", Region.class);

		Product ipod;
		Product ipad;
		Product macbook;
		/*
		 * Create some products if the region is empty
		 */
		if (productRegion.size() == 0) {
			log.debug("No entries found in Product region. Creating some...");
			ipod = new Product(1L, "Apple iPod", new BigDecimal(99.99),
					"An Apple portable music player");
			ipad = new Product(2L, "Apple iPad", new BigDecimal(499.99),
					"An Apple tablet device");
			macbook = new Product(3L, "Apple macBook", new BigDecimal(899.99),
					"An Apple notebook computer");
			macbook.setAttribute("warantee", "included");

			productRegion.put(ipad.getId(), ipad);
			productRegion.put(ipod.getId(), ipod);
			productRegion.put(macbook.getId(), macbook);
		} 
		
		log.debug("Product region contains " + productRegion.size() + " entries");
		 
		if (orderRegion.size() == 0) {
			
			log.debug("No entries found in Order region. Creating some...");
			// Write some random orders

			Random random = new Random(new Date().getTime());
			Address address = new Address("it", "doesnt", "matter");
			for (long orderId = 1; orderId <= 1000; orderId++) {

				Order order = new Order(orderId, 0L, address);
				int nLineItems = 5;
				for (int i = 0; i < nLineItems; i++) {
					int quantity = random.nextInt(3) + 1;
					long productId = random.nextInt(3) + 1;
					order.add(new LineItem(productRegion.get(productId),
							quantity));
				}
				orderRegion.put(orderId, order); 
			}
		}
			
		log.debug("Order region contains " + orderRegion.size() + " entries");
		log.debug("eviction attributes:" + orderRegion.getAttributes().getEvictionAttributes()); 
	}

	/**
	 * 
	 */
	private static void createDiskStoreDirectories() {
		File orders = new File("orders");
		if (!orders.exists()){
			orders.mkdir();
		}
		File products = new File("products");
		if (!products.exists()){
			products.mkdir();
		}
	}

	/**
	 * @throws IOException
	 * 
	 */
	private static void deleteDiskStoresIfRequested() throws IOException {
		String ans = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (!(ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("n"))) {
			System.out.print("Do you want to delete the disk stores [y,N]? :");
			ans = br.readLine();
			if (ans.trim().length() == 0) {
				ans = "n";
			}
		}

		if (ans.equalsIgnoreCase("y")) {
			log.debug("deleting disk stores...");

			File ordersDir = new File("orders");
			for (File file : ordersDir.listFiles()) {
				file.delete();
			}
			File productsDir = new File("products");
			for (File file : productsDir.listFiles()) {
				file.delete();
			}
		}
	}
}
