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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;
/**
 * An example order application
 * @author David Turanski
 *
 */
public class OrderExample {
	private static Log log = LogFactory.getLog(OrderExample.class);
	
	private final CustomerService customerService;
	private final ProductService productService;
	private final OrderService orderService;

	/**
	 * Create the sample application, injecting required services
	 * @param customerService
	 * @param productService
	 * @param orderService
	 */
	public OrderExample(CustomerService customerService, ProductService productService, OrderService orderService) {
		this.customerService = customerService;
		this.productService = productService;
		this.orderService = orderService;
	}
	
	/**
	 * Run the sample application
	 */
	public void run(){
		createCustomers();
		createProducts();
		createOrders();
		
		findCustomers();
		findProducts();
		findOrders();
		
		deleteOrders();
		deleteProducts();
		deleteCustomers();
	}

	

	private void deleteCustomers() {
		for (Customer customer: customerService.findAllCustomers()){
			customerService.deleteCustomer(customer);
		}	
	}

	private void deleteProducts() {
		for (Product product: productService.findAllProducts()){
			productService.deleteProduct(product);
		}
	}

	private void deleteOrders() {
		for (Order order: orderService.findAllOrders()){
			orderService.deleteOrder(order);
		}
	}

	private void findOrders() {
		log.debug("looking for orders for customer ID 2");
		
		for (Order order: orderService.findOrdersByCustomerId(2L)) {
			log.debug("found order ID " + order.getId() + " " + 
					order.getBillingAddress().getStreet() + " " + 
					order.getBillingAddress().getCity() + " " + 
					order.getBillingAddress().getCountry());
				for (LineItem lineItem: order.getLineItems()) {
					log.debug("product ID:" + lineItem.getProductId() + 
						  " quantity:" + lineItem.getAmount() + 
						  " unit price:" + lineItem.getUnitPrice().setScale(2,BigDecimal.ROUND_DOWN) + 
						  " total price:" + lineItem.getTotal().setScale(2,BigDecimal.ROUND_DOWN));
				}
		}
	}

	private void findProducts() {
		log.debug("looking for products with description containing 'Apple'");
		for (Product product: productService.findProductsByDescription("Apple")) {
			log.debug("found " + product.getName() + " " + product.getDescription());
		}
		
		log.debug("looking for products with description containing 'notebook'");
		for (Product product: productService.findProductsByDescription("notebook")) {
			log.debug("found " + product.getName() + " " + product.getDescription());
		}
		
		log.debug("looking for products with warantee included");
		for (Product product: productService.findProductsByAttribute("warantee", "included")){
			log.debug("found " + product.getName() + " " + product.getDescription());
		}
		
	}

	private void findCustomers() {
		log.debug("looking for all customers with last name 'Matthews'");
		for (Customer customer: customerService.findCustomersByLastName("Matthews")){
		   log.debug("found :" + customer.getFirstname() + " " + customer.getLastname());
		}
	}

	/*
	 * create some products
	 */
	private void createProducts() {
		Product ipod = new Product(1L,"Apple iPod",new BigDecimal(99.99),"An Apple portable music player");
		Product ipad = new Product(2L,"Apple iPad",new BigDecimal(499.99),"An Apple tablet device");
		Product macbook = new Product(3L,"Apple macBook",new BigDecimal(899.99),"An Apple notebook computer");
		macbook.setAttribute("warantee","included");
		
		productService.createProduct(ipod);
		productService.createProduct(ipad);
		productService.createProduct(macbook);
		
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
		
		Order davesOrder = new Order(1L, dave.getId(),new Address("Dave Street","Matthews","USA"));
		
		Product iPad = productService.findProductsByName("Apple iPad").get(0);
		Product macBook = productService.findProductsByName("Apple macBook").get(0);
		Product iPod = productService.findProductsByDescription("notebook").get(0);
		
		davesOrder.add(new LineItem(iPad,2));
		davesOrder.add(new LineItem(macBook));
		
		orderService.createOrder(davesOrder);
		
		Customer alicia = customerService.findByEmailAddress("alicia@keys.com");
		
		Order aliciasFirstOrder = new Order(2L, alicia.getId(),new Address("Alicia Street","Keys","USA"));
		
		aliciasFirstOrder.add(new LineItem(iPod,3));
		
		orderService.createOrder(aliciasFirstOrder);
		
		Order aliciasNextOrder = new Order(3L, alicia.getId(),new Address("Alicia Street","Keys","USA"));
		
		aliciasNextOrder.add(new LineItem(macBook,4));
		aliciasNextOrder.add(new LineItem(iPad));
		
		orderService.createOrder(aliciasNextOrder);
		
	}
}
