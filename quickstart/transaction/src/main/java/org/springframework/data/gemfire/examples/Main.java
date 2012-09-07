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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class Main {
	private static Log log = LogFactory.getLog(Main.class);
	private static CustomerService customerService;

	public static void main(String[] args)  {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		
		customerService = context.getBean(CustomerService.class);
		
		Customer customer = new Customer(1L,new EmailAddress("customer@customer.com"),"First","Last");
		log.debug("creating a Customer with last name " + customer.getLastname());
		
		customerService.updateCustomer(customer,false);
		
		customer = customerService.getCustomer(1L);
		
		customer.setLastname("Newname");
		
		try {
			log.debug("changing customer last name to " + customer.getLastname());
			customerService.updateCustomer(customer,true);
			
		} catch (Exception e) {
			customer = customerService.getCustomer(1L);
			Assert.isTrue(customer.getLastname().equals("Last"), "No rollback - customer last name is " + customer.getLastname());
			log.debug(e.getMessage() + " Customer last name is still '" + customer.getLastname()+"'");
		}
	}
}


