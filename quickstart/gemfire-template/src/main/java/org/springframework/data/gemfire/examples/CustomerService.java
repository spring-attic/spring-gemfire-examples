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
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * A simple Customer service implementation
 * @author David Turanski
 *
 */
@Component
public class CustomerService {
	private static Log log = LogFactory.getLog(CustomerService.class);
	@Autowired 
	CustomerDao customerDao;
	@Autowired
	OrderDao orderDao;

	/**
	 * Delete Customer and any customer orders. This runs in a transaction
	 * 
	 * @param customerId
	 */
	@Transactional("gemfireTransactionManager")
	public void deleteCustomer(long customerId) {
		log.debug("deleting customer ID " + customerId);
		customerDao.delete(customerId);
		for (Order order: orderDao.findCustomerOrders(customerId)) {
			orderDao.delete(order.getId());
		}
		log.debug("deleted customer ID " + customerId);
	}
	
	/**
	 * Create a customer
	 * @param customer
	 * @return
	 */
	public Customer createCustomer(Customer customer) {
		Assert.notNull(customer,"customer cannot be null");
		Assert.notNull(customer.getId(), "customer ID cannot be null");
		Assert.hasText(customer.getLastname(),"customer last name cannot be empty");
		Assert.hasText(customer.getFirstname(),"customer first name cannot be empty");
		Assert.notNull(customer.getEmailAddress(),"customer email address cannot be null");
		
		log.debug("saving customer ID:" + customer.getId() + " " + customer.getFirstname() + " " + customer.getLastname());
		
		return customerDao.save(customer);
	}
	
	/**
	 * Find a customer by email address
	 * @param emailAddress
	 * @return
	 */
	public Customer findByEmailAddress(String emailAddress) {
		Assert.hasText(emailAddress,"email address cannot be null or blank");
		return customerDao.findByEmailAddress(new EmailAddress(emailAddress));
	}
	
}
