/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.data.gemfire.examples.client;

import java.math.BigDecimal;

import org.springframework.data.gemfire.function.annotation.OnServer;



/**
 * @author David Turanski
 *
  
 * By default this will run on a server connected to the default client cache. e.g., the declaration is equivalent to 
 * <code>
 * @OnServer(id="totalSalesForProduct",cache="gemfireCache")
 * </code>
 * 
 * Also since the interface is a singleton, the method invocation is associated with the function with the same Id.
 * 
 */
@OnServer
public interface SalesCalculator {
 	public BigDecimal totalSalesForProduct(String productName);
}
