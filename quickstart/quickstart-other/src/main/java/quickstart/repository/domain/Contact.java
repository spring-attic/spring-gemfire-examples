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
@Region("contacts")
public class Contact {
	@Id
	private int id;

	private String firstName;

	private String lastName;

	private String primaryEmail;

	private String secondaryEmail;

	private String instantMessage;

	private List<Phone> phoneNumbers;

	public int getId() {
		return id;
	}

	public Contact setId(int id) {
		this.id = id;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public Contact setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public Contact setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public Contact setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
		return this;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public Contact setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
		return this;
	}

	public String getInstantMessage() {
		return instantMessage;
	}

	public Contact setInstantMessage(String instantMessage) {
		this.instantMessage = instantMessage;
		return this;
	}

	public List<Phone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public Contact setPhoneNumbers(List<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
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
