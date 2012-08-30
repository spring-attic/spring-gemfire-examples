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
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.stereotype.Repository;

import com.gemstone.gemfire.cache.query.SelectResults;

/**
 * An Implementation of {@link CustomerDao} using {@link GemfireTemplate} 
 * @author David Turanski
 *
 */
@Repository
public class GemfireTemplateCustomerDao implements CustomerDao {
	@Autowired
	GemfireTemplate customerTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.data.gemfire.examples.CustomerDao#delete(long)
	 */
	@Override
	public void delete(long id) {
		customerTemplate.remove(id);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.data.gemfire.examples.CustomerDao#save(org.springframework.data.gemfire.examples.domain.Customer)
	 */
	@Override
	public Customer save(Customer customer) {
		return customerTemplate.put(customer.getId(),customer);
	}

	@Override
	public Customer get(long id) {
		return customerTemplate.get(id);
	}

	@Override
	public Customer findByEmailAddress(EmailAddress emailAddress) {
		SelectResults<Customer> customers =  customerTemplate.find("SELECT * from /Customer WHERE emailAddress=$1",emailAddress);
		return customers.isEmpty()? null: customers.asList().get(0);
	}
	
}
