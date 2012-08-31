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


import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
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
import org.springframework.data.gemfire.examples.util.ServerPortGenerator;

import com.gemstone.gemfire.cache.Region;

public class Server {
	private static Log log = LogFactory.getLog(Server.class);
	
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException {
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
		
		Region<Long,Product> productRegion = context.getBean("Product",Region.class);
		Region<Long,Order> orderRegion = context.getBean("Order",Region.class);
		//create some products
		
		Product ipod = new Product(1L,"Apple iPod",new BigDecimal(99.99),"An Apple portable music player");
		Product ipad = new Product(2L,"Apple iPad",new BigDecimal(499.99),"An Apple tablet device");
		Product macbook = new Product(3L,"Apple macBook",new BigDecimal(899.99),"An Apple notebook computer");
		macbook.setAttribute("warantee","included");
		
		productRegion.put(ipad.getId(), ipad);
		productRegion.put(ipod.getId(), ipod);
		productRegion.put(macbook.getId(), macbook);
		
		
		//Write some random orders
		
		Random random = new Random(new Date().getTime());
		Address address = new Address("it","doesnt","matter");
		for (long orderId = 1; orderId <= 100; orderId ++) {
			
			Order order = new Order(orderId,0L,address);
			int nLineItems  = random.nextInt(3) + 1;
			for (int i = 0; i<nLineItems; i++){
				int quantity = random.nextInt(3) + 1;
				long productId = random.nextInt(3) + 1;
				log.debug("creating line item for product id " + productId + " quantity " + quantity);
				order.add(new LineItem(productRegion.get(productId),quantity));
			} 
			orderRegion.put(orderId,order);
		}
		
		System.out.println("Press <Enter> to terminate the server");
		System.in.read();
		System.exit(0);
	}

	
}
