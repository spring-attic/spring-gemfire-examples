/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.data.gemfire.examples;

import java.io.Serializable;

/**
 * @author David Turanski
 *
 */
@SuppressWarnings("serial")
public class OrderKey implements Serializable {
	final String countryCode;
	final Long id;
	
	public OrderKey(Long id, String countryCode) {
		this.id = id;
		this.countryCode = countryCode;
	}
	
	public int hashCode() {
		return id.hashCode();
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof OrderKey)) {
			return false;
		}
		return ((OrderKey)other).id.equals(this.id);
	}
	
	public String toString() {
		return id + ":" + countryCode;
	}
}
