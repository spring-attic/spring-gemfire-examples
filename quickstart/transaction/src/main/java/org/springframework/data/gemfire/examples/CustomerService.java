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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author David Turanski
 * 
 */
public class CustomerService {
	
	@Autowired CustomerRepository customerRepository;
	
	/**
	 * @param customer
	 * @param fail - set to true to simulate some downstream processing that throws an exception which should trigger a rollback 
	 */
	@Transactional("gemfireTransactionManager")	
	public void updateCustomer(Customer customer, boolean fail) {
		 customerRepository.save(customer);
		 if (fail) {
			 throw new RuntimeException("Updated failed - should trigger a rollback.");
		 } 
	}
	
	public Customer getCustomer(long id) {
		return customerRepository.findOne(id);
	}
}
