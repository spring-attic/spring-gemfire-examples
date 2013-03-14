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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.gemfire.GemfireOperations;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.query.SelectResults;
/**
 * Sample application demonstrating JSON support
 * @author David Turanski
 *
 */
@Component
public class JSONCustomerExample {
	private static Log log = LogFactory.getLog(JSONCustomerExample.class);
	@Autowired
	@Qualifier("customerTemplate")
	GemfireOperations customerTemplate;
	/**
	 * run the example
	 */
	public void run() {
		createCustomers();
		searchCustomers();
		deleteCustomers();
	}

	/*
	 * create some customers
	 */
	private void createCustomers() {
		Customer dave = new Customer(1L,new EmailAddress("dave@matthews.com"),"Dave","Matthews");
		Customer alicia = new Customer(2L,new EmailAddress("alicia@keys.com"),"Alicia","Keys");
		customerTemplate.put(1L,dave);
		customerTemplate.put(2l, alicia);
	}
	
	/*
	 * Retrieve customers
	 */
	private void searchCustomers() {
		String jSonDave = (String) customerTemplate.findUnique("select * from /Customer where emailAddress.value=$1","dave@matthews.com");
		log.info(jSonDave);
		
		SelectResults<String> results = customerTemplate.find("select * from /Customer where emailAddress.value=$1", "alicia@keys.com");
		String jSonAlicia = results.iterator().next();
		log.info(jSonAlicia);
	}

	/*
	 * Delete customers
	 */
	private void deleteCustomers() {
		customerTemplate.remove(1L);
		customerTemplate.remove(2L);
	}
}
