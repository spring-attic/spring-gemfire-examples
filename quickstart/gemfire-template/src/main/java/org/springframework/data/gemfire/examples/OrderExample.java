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

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.stereotype.Component;

/**
 * Sample application demonstrating the use of GemfireTemplate
 * @author David Turanski
 *
 */
@Component
public class OrderExample {
	private final OrderService orderService;
	private final CustomerService customerService;

	/**
	 * Create the example with depend services
	 * @param orderService
	 * @param customerService
	 */
	@Autowired
	OrderExample(OrderService orderService, CustomerService customerService) {
		this.orderService = orderService;
		this.customerService = customerService;
	}
	
	/**
	 * run the example
	 */
	public void run() {
		createCustomers();
		createOrders();
		deleteCustomers();
	}

	/*
	 * create some customers
	 */
	private void createCustomers() {
		Customer dave = new Customer(1L,new EmailAddress("dave@matthews.com"),"Dave","Matthews");
		Customer alicia = new Customer(2L,new EmailAddress("alicia@keys.com"),"Alicia","Keys");
		customerService.createCustomer(dave);
		customerService.createCustomer(alicia);
	}
	
	/*
	 * Create some customer orders
	 */
	private void createOrders() {
		Customer dave = customerService.findByEmailAddress("dave@matthews.com");
		
		Product ipod = new Product(1L,"Apple iPod",new BigDecimal(99.99),"An Apple portable music player");
		Product ipad = new Product(2L,"Apple iPad",new BigDecimal(499.99),"An Apple tablet device");
		Product macbook = new Product(3L,"Apple macBook",new BigDecimal(899.99),"An Apple notebook computer");
		macbook.setAttribute("warantee","included");
		
		Order davesOrder = new Order(1L, dave.getId(),new Address("Dave Street","Matthews","USA"));
		
		davesOrder.add(new LineItem(ipad,2));
		davesOrder.add(new LineItem(macbook));
		
		orderService.createOrder(davesOrder);
		
		Customer alicia = customerService.findByEmailAddress("alicia@keys.com");
		
		Order aliciasFirstOrder = new Order(2L, alicia.getId(),new Address("Alicia Street","Keys","USA"));
		
		aliciasFirstOrder.add(new LineItem(ipod,3));
		
		orderService.createOrder(aliciasFirstOrder);
		
		Order aliciasNextOrder = new Order(3L, alicia.getId(),new Address("Alicia Street","Keys","USA"));
		
		aliciasNextOrder.add(new LineItem(macbook,4));
		aliciasNextOrder.add(new LineItem(ipad));
		
		orderService.createOrder(aliciasNextOrder);
		
	}
	
	/*
	 * Delete customers and there orders
	 */
	private void deleteCustomers() {
		customerService.deleteCustomer(1L);
		customerService.deleteCustomer(2L);
	}
}
