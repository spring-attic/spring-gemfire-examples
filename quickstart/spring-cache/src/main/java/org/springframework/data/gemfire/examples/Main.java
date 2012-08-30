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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Customer;

public class Main {
	private static Log log = LogFactory.getLog(Main.class);

	public static void main(String args[]) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("app-context.xml");
		
		CustomerService customerService = context.getBean(CustomerService.class);
	
		log.info("Retrieving objects not in cache...");
		for (long i = 1; i<=2; i++) {
			log.info("Retrieving object with id: " + i);
			Customer c = customerService.findCustomer(i);
			log.info("Retrieved " + c.getFirstname()+ " " + c.getLastname());
		}
		
		log.info("Retrieving the same objects again. This time, the target method is not actually invoked!");  
		log.info("@Cacheable causes Spring to wrap CustomerService in a proxy: " + customerService.getClass().getName());

		for (int i = 1; i<=2; i++) {
			log.info("Retrieving object with id: " + i);
			Customer c = customerService.findCustomer(i);
			log.info("Retrieved " + c.getFirstname()+ " " + c.getLastname());
		}
	}
}
