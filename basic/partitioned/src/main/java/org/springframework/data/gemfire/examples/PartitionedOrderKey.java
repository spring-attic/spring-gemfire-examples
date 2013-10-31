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

import com.gemstone.gemfire.cache.EntryOperation;
import com.gemstone.gemfire.cache.PartitionResolver;

/**
 * @author David Turanski
 *
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class PartitionedOrderKey extends OrderKey implements PartitionResolver {


	public PartitionedOrderKey(Long id, String countryCode) {
		super(id,countryCode);
	}
	@Override
	public void close() {
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.PartitionResolver#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.PartitionResolver#getRoutingObject(com.gemstone.gemfire.cache.EntryOperation)
	 */
	@Override
	public Object getRoutingObject(EntryOperation op) {
		return this.countryCode;
	}
}
