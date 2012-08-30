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

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.stereotype.Component;

/**
 * Retrieve Customer data from a simulated data store
 * @author David Turanski
 *
 */
@Component
public class HashMapCustomerDao implements CustomerDao {
	private Map<Long,Customer> customerData;
	
	public HashMapCustomerDao() {
		customerData = new HashMap<Long,Customer>();
		Customer dave = new Customer(1L,new EmailAddress("dave@matthews.com"),"Dave","Matthews");
		Customer alicia = new Customer(2L,new EmailAddress("alicia@keys.com"),"Alicia","Keys");
		customerData.put(dave.getId(),dave);
		customerData.put(alicia.getId(), alicia);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.data.gemfire.examples.CustomerDao#get(long)
	 */
	@Override
	public Customer get(long id){
		return customerData.get(id);
	}
}
