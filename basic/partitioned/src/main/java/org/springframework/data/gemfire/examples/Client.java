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
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Order;

import com.gemstone.gemfire.cache.Region;

public class Client {
	
	
	@SuppressWarnings("unchecked")
	
	public static void main(String args[]) throws IOException {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("client/cache-config.xml");
		Region<Long,Order> region = context.getBean(Region.class);
	
		//Create some orders
		Random rand = new Random(100); 
		for (long orderId = 1; orderId <= 100; orderId++) {
			Address shipTo = new Address("Some Street","Some City",(orderId%2 == 0)?"US":"UK"); 
			Order order = new Order(orderId, rand.nextLong(),shipTo);
			region.put(orderId, order, order);
		}
		 
	}
}
