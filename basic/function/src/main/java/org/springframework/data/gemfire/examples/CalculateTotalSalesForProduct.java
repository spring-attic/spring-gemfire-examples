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

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.ResultSender;
import com.gemstone.gemfire.cache.query.SelectResults;


/**
 * A GemFire Function for aggregating and calculating results on the cache server. 
 * 
 * This function computes the total sales for a given product.
 * 
 * @author David Turanski
 *
 */
@SuppressWarnings("serial")
@Component
public class CalculateTotalSalesForProduct extends FunctionAdapter {
	private static Log log = LogFactory.getLog(CalculateTotalSalesForProduct.class);

	@Resource(name="productTemplate")
	private GemfireTemplate productTemplate; 
	@Resource(name="Order")
	private Region<Long,Order> orderRegion;
	
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.FunctionAdapter#execute(com.gemstone.gemfire.cache.execute.FunctionContext)
	 */
	@Override
	public void execute(FunctionContext functionContext) {
		ResultSender<BigDecimal> resultSender = functionContext.getResultSender();
		
		String productName = (String)functionContext.getArguments();
		
		log.debug("searching for product name '" + productName + "'");
		
		SelectResults<Product> results = productTemplate.query("name = '" + productName + "'");

		if (results.isEmpty()) {
			log.warn("cannot find product '" + productName + "'");
			resultSender.lastResult(new BigDecimal(0.0));
			return;
		}
		
		Product product = results.asList().get(0);
		
		long productId = product.getId();
		
		BigDecimal total = new BigDecimal(0.0);
		
		for (Order order: orderRegion.values()) {
			for (LineItem lineItem: order.getLineItems()) {
				if (lineItem.getProductId() == productId) {
					total = total.add(lineItem.getTotal());
				}
			}
		}
		
		resultSender.lastResult(total.setScale(2,BigDecimal.ROUND_CEILING));
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.FunctionAdapter#getId()
	 */
	@Override
	public String getId() {
		return getClass().getSimpleName();
	}
}
