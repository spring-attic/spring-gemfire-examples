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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.repository.OrderRepository;
import org.springframework.util.Assert;

 

/**
 * A simple Order service
 * 
 * @author David Turanski
 *
 */
public class OrderService {
	private static Log log = LogFactory.getLog(OrderService.class);
	@Autowired OrderRepository orderRepository;
	
	/**
	 * Create an order
	 * @param order
	 * @return
	 */
	public Order createOrder(Order order) {
		Assert.notNull(order,"order cannot be null");
		Assert.notNull(order.getId(),"order ID cannot be null");
		Assert.notNull(order.getCustomerId(),"order customer ID cannot be null");
		Assert.notEmpty(order.getLineItems(),"order must contain at least one line item");
		Assert.notNull(order.getBillingAddress(),"order billing address cannot be null");
		
		log.debug("creating new order "+ order.getId());
		return orderRepository.save(order);
	}
	
	/**
	 * Delete an order 
	 * @param id
	 * @return
	 */
	public boolean deleteOrder(long id) {
		Order order = orderRepository.findOne(id);
		if (order != null ){
			orderRepository.delete(order);
			log.debug("deleted order :" + order.getId() );
			return true;
		}
		log.debug("cannot find order for id " + id);
		return false;
	}
	
	/**
	 * Delete an order
	 * @param order
	 * @return
	 */
	public boolean deleteOrder(Order order) {
		Assert.notNull(order,"order cannot be null");
		Assert.notNull(order.getId(),"order ID cannot be null");
		return deleteOrder(order.getId());
	}
	
	/**
	 * Find all orders for a customer
	 * @param customerId
	 * @return
	 */
	public List<Order> findOrdersByCustomerId(long customerId) {
		return orderRepository.findByCustomerId(customerId);
	}
	
	 
	/**
	 * Find all orders
	 * @return
	 */
	public List<Order> findAllOrders() {
		List<Order> orders = new ArrayList<Order>();
		for (Order order: orderRepository.findAll()) {
			orders.add(order);
		}
		return Collections.unmodifiableList(orders);
	}
}
