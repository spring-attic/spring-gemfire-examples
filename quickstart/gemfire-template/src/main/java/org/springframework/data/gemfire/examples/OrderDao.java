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

import java.util.List;

import org.springframework.data.gemfire.examples.domain.Order;
/**
 * Interface for Order data access
 * @author David Turanski
 *
 */
public interface OrderDao {

	/**
	 * find all orders for a customer
	 * @param customerId
	 * @return
	 */
	public abstract List<Order> findCustomerOrders(long customerId);

	/**
	 * Delete an order
	 * @param id
	 */
	public abstract void delete(long id);

	/**
	 * Save an order
	 * @param order
	 * @return
	 */
	public abstract Order save(Order order);
	
	/**
	 * Retrieve an order by ID
	 * @param id
	 * @return
	 */
	public abstract Order get(long id);

}