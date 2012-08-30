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
package quickstart.repository.domain;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.Region;

/**
 * @author David Turanski
 * 
 */
@Region("customers")
public class Customer {
	@Id
	private int id;

	private String name;

	private List<Address> addresses;

	private List<Contact> contacts;

	public Customer(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public Customer setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Customer setName(String name) {
		this.name = name;
		return this;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public Customer setAddresses(List<Address> addresses) {
		this.addresses = addresses;
		return this;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public Customer setContacts(List<Contact> contacts) {
		this.contacts = contacts;
		return this;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(this);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
