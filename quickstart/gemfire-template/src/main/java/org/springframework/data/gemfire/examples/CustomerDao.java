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

import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;

/**
 * Interface for Customer data access
 * @author David Turanski
 *
 */
public interface CustomerDao {

	/**
	 * Delete a customer by ID
	 * @param id
	 */
	public abstract void delete(long id);

	/**
	 * Save a customer
	 * @param customer
	 * @return
	 */
	public abstract Customer save(Customer customer);
	
	/**
	 * Retrieve a customer by ID
	 * @param id
	 * @return
	 */
	public abstract Customer get(long id);
	
	/**
	 * Find a Customer by email address
	 * @param emailAddress
	 * @return
	 */
	public abstract Customer findByEmailAddress(EmailAddress emailAddress);

}