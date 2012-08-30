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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.stereotype.Component;

@Component
/**
 * A Service for accessing Customer Data
 * @author "David Turanski"
 *
 */
public class CustomerService {
	private static Log log = LogFactory.getLog(CustomerService.class);
	@Autowired
	CustomerDao customerDao;
	
	@Cacheable("Customer")
	public Customer findCustomer(long id) {
		log.info("Retrieving a customer instance from the data store on a cache miss. The return value will be cached");
		return customerDao.get(id);
	}
}
