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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.data.gemfire.examples.repository.CustomerRepository;
import org.springframework.util.Assert;

/**
 * A simple Customer service
 * @author David Turanski
 *
 */
public class CustomerService {
	private static Log log = LogFactory.getLog(CustomerService.class);
	@Autowired
	CustomerRepository customerRepository;
	
	/**
	 * Create a new customer
	 * @param customer
	 * @return
	 */
	public Customer createCustomer(Customer customer) {
		Assert.notNull(customer, "customer cannot be null");
		Assert.notNull(customer.getId(),"customer ID cannot be null");
		Assert.hasText(customer.getFirstname(),"customer first name must contain text");
		Assert.hasText(customer.getLastname(),"customer last name must contain text");
		Assert.notNull(customer.getEmailAddress(),"customer email address must not be null");
		
		Customer newCustomer =  customerRepository.save(customer);
		log.debug("Created new customer " + customer.getFirstname()+ " " + customer.getLastname());
		return newCustomer;
	}
	
	/**
	 * Delete a customer with a given id;
	 * @param id
	 * @return
	 */
	
	public boolean deleteCustomer(long id) {
		Customer customer = customerRepository.findOne(id);
		if (customer != null) {
			customerRepository.delete(customer);
			log.debug("deleted customer " + customer.getId()+ ":" + customer.getFirstname()+ " " + customer.getLastname());
			return true;
		} 
		log.warn("Customer not found for id " + id);
		return false;
	}
	
	/**
	 * Delete a customer
	 * @param customer
	 */
	public boolean deleteCustomer(Customer customer) {
		Assert.notNull(customer, "customer cannot be null");
		Assert.notNull(customer.getId(),"customer ID cannot be null");
		return deleteCustomer(customer.getId());
	}
	
	/**
	 * Retrieve a customer by id
	 * @param id
	 * @return
	 */
	public Customer getCustomer(long id) {
		return customerRepository.findOne(id);
	}
	
	/**
	 * Retrieve a list of customers by last name
	 * @param lastname
	 * @return
	 */
	public List<Customer> findCustomersByLastName(String lastname) {
		return customerRepository.findByLastname(lastname);
	}
	
	/**
	 * Retrieve a customer by emailAddress
	 * @param emailAddress
	 * @return
	 */
	public Customer findByEmailAddress(String emailAddress) {
		return customerRepository.findByEmailAddress(new EmailAddress(emailAddress));
	}
	
	/**
	 * Return all customers
	 * @return
	 */
	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}
}
