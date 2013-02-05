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
package org.springframework.data.gemfire.examples.server;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.query.SelectResults;


/**
 * A function for aggregating and calculating results on the cache server.  * 
 * 
 * @author David Turanski
 *
 */

@Component
public class SalesFunctions {
	private static Log log = LogFactory.getLog(SalesFunctions.class);

	@Resource(name="productTemplate")
	private GemfireTemplate productTemplate; 
	@Resource(name="Order")
	private Region<Long,Order> orderRegion;
 

	/**
	 * This method computes total sales for a given product. The <code>@GemfireFunction</code> 
	 * annotation allows this function to be wrapped in a GemFire {@link Function} and registered with the given 
	 * id.
	 * 
	 * @param productName
	 * @return
	 */
	@GemfireFunction
	public BigDecimal totalSalesForProduct(String productName) {
		
		log.debug("searching for product name '" + productName + "'");
		
		SelectResults<Product> results = productTemplate.query("name = '" + productName + "'");

		if (results.isEmpty()) {
			log.warn("cannot find product '" + productName + "'");
			return new BigDecimal(0.0);
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
		
		return total.setScale(2,BigDecimal.ROUND_CEILING);
	}
}
