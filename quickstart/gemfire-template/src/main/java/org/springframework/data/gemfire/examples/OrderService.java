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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A simple Order service
 * @author David Turanski
 *
 */
@Component
public class OrderService {
	private static Log log = LogFactory.getLog(OrderService.class);
	
	@Autowired OrderDao orderDao;
	
	/**
	 * Create an order
	 * @param order
	 * @return
	 */
	public Order createOrder(Order order) {
		Assert.notNull(order, "order cannot be null");
		Assert.notNull(order.getId(),"order id cannot be null");
		Assert.notNull(order.getCustomerId(), "order customer ID cannot be null");
		Assert.notEmpty(order.getLineItems(),"order must contain at least one line item");
		
		log.debug("creating order ID:" + order.getId() );
		return orderDao.save(order);
		
	}
	
	/**
	 * Get an Order by ID
	 * @param id
	 * @return
	 */
	public Order getOrder(long id) {
		return orderDao.get(id);
	}
}
