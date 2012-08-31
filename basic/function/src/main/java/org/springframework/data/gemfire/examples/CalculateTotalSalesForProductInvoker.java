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

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

/**
 * Invokes a registered function using GemFire's {@link FunctionService}
 * @author David Turanski
 *
 */
@Component
public class CalculateTotalSalesForProductInvoker {
	@Resource(name="gemfirePool")
	private Pool pool;

	/**
	 * Calculate total sales for the given product
	 * @param productName the name of the product
	 * @return
	 */
	public BigDecimal forProduct(String productName) {
		
		Execution functionExecution = FunctionService.onServer(pool)
				.withArgs(productName);
		
		ResultCollector<?,?> results = functionExecution.execute("CalculateTotalSalesForProduct");
		
		List<?> list = (List<?>)results.getResult();
		
		return ((BigDecimal)list.get(0));
	}
}
