/*
 * Copyright 2010-2012 the original author or authors.
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
package quickstart.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import quickstart.repository.domain.Contact;
import quickstart.repository.domain.Customer;
import quickstart.repository.domain.Phone;
import quickstart.repository.domain.Phone.Type;
import quickstart.repository.repositories.ContactRepository;
import quickstart.repository.repositories.CustomerRepository;

/**
 * @author David Turanski
 * 
 */
@Component
public class CustomerClient {
	private static Log log = LogFactory.getLog(CustomerClient.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ContactRepository contactRepository;

	public void start() {
		populateCustomers();
		log.debug("Customer repository initialized with " + customerRepository.count() + " customers.");
		for (Customer customer : customerRepository.findAll()) {
			log.debug(customer);
		}

		log.debug("found: " + customerRepository.findOne(1));

		log.debug("Contact repository initialized with " + contactRepository.count() + " contacts.");
	}

	private void populateCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		Phone phone = new Phone("(212)-555-1212", Type.WORK);
		List<Phone> phoneNumbers = new ArrayList<Phone>();
		phoneNumbers.add(phone);

		Contact contact = new Contact().setId(1).setFirstName("David").setLastName("Turanski")
				.setPrimaryEmail("dturanski@vmware.com").setPhoneNumbers(phoneNumbers);

		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(contact);

		customers.add(new Customer(1, "VMWare").setContacts(contacts));
		customers.add(new Customer(2, "SpringSource"));
		customers.add(new Customer(3, "Gemfire"));
		customers.add(new Customer(4, "Spring Data"));

		for (Customer customer : customers) {
			customerRepository.save(customer);
		}
	}
}
