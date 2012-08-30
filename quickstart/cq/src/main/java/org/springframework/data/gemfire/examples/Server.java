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
import java.math.BigDecimal;
import java.net.ServerSocket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.examples.util.ServerPortGenerator;

import com.gemstone.gemfire.cache.Region;

/**
 * Creates a cache server in this process and adds some data to the cache
 * @author David Turanski
 *
 */
public class Server {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException,
			InterruptedException {
		
		/*
		 *  Check if port is open. Currently the client pool is hard coded to look for a server on 40404, the default. If already taken, 
		 *  this process will wait for a while so this forces an immediate exit if the port is in use. There are better ways to handle this 
		 *  situation, but hey, this is sample code.   
		 */
		try {
			new ServerPortGenerator().bind(new ServerSocket(), 40404,1);
		} catch (IOException e) {
			System.out.println("Sorry port 40404 is in use. Do you have another cache server process already running?");
			System.exit(1);
			
		}
		
		ApplicationContext context = new ClassPathXmlApplicationContext("server/cache-config.xml");

		Region<Long, Order> region = context.getBean("Order", Region.class);
		
	 
		/*
		 * Create some customer orders
		 */

		Product ipod = new Product(1L, "Apple iPod", new BigDecimal(99.99),
				"An Apple portable music player");
		Product ipad = new Product(2L, "Apple iPad", new BigDecimal(499.99),
				"An Apple tablet device");
		Product macbook = new Product(3L, "Apple macBook", new BigDecimal(
				899.99), "An Apple notebook computer");
		macbook.setAttribute("warantee", "included");

		Order davesOrder = new Order(1L, 1L, new Address(
				"Dave Street", "Matthews", "USA"));

		davesOrder.add(new LineItem(ipad, 2));
		davesOrder.add(new LineItem(macbook));

		Order aliciasFirstOrder = new Order(2L, 2L, new Address(
				"Alicia Street", "Keys", "USA"));

		aliciasFirstOrder.add(new LineItem(ipod, 3));

		Order aliciasNextOrder = new Order(3L, 2L, new Address(
				"Alicia Street", "Keys", "USA"));

		aliciasNextOrder.add(new LineItem(macbook, 4));
		aliciasNextOrder.add(new LineItem(ipad));

		

		System.out.println("Press <ENTER> to update cache");
		System.in.read();
		
		region.put(davesOrder.getId(), davesOrder);
		region.put(aliciasFirstOrder.getId(), aliciasFirstOrder);
		region.put(aliciasNextOrder.getId(), aliciasNextOrder);

		System.out.println("Press <ENTER> to terminate the cache server");
		System.in.read();
		System.exit(0);
	}
}
