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

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.gemfire.examples.domain.Order;

import com.gemstone.gemfire.cache.EntryOperation;
import com.gemstone.gemfire.cache.FixedPartitionResolver;
import com.gemstone.gemfire.cache.PartitionResolver;

/**
 * @author David Turanski
 *
 */
public class CountryPartitionResolver implements FixedPartitionResolver<Object, Order>{
	private static Log log = LogFactory.getLog(CountryPartitionResolver.class);
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheCallback#close()
	 */

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.PartitionResolver#getName()
	 */
	@Override
	public String getName() {
		 return getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.PartitionResolver#getRoutingObject(com.gemstone.gemfire.cache.EntryOperation)
	 */
	@Override
	public Object getRoutingObject(EntryOperation<Object, Order> operation) {
		return operation.getKey();
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheCallback#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.FixedPartitionResolver#getPartitionName(com.gemstone.gemfire.cache.EntryOperation, java.util.Set)
	 */
	@Override
	public String getPartitionName(EntryOperation<Object, Order> operation,
			Set<String> targetPartitionNames) {
		if (operation.getCallbackArgument() != null){
			Order order = (Order)operation.getCallbackArgument();
			String country = order.getBillingAddress().getCountry();
			log.debug("partition name resolved to " + country);
			return country;
		}
		//Default
		return "US";
	}
}
