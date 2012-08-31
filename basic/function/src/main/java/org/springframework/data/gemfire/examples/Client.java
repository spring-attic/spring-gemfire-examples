package org.springframework.data.gemfire.examples;
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


import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {

	public static void main(String args[]) throws IOException {
		Log log = LogFactory.getLog(Client.class);
		ApplicationContext context = new ClassPathXmlApplicationContext("client/cache-config.xml");
		
		CalculateTotalSalesForProductInvoker calculateTotalForProduct = context.getBean(CalculateTotalSalesForProductInvoker.class);
		
		String[] products = new String[]{"Apple iPad","Apple iPod","Apple macBook"};
		
		for (String productName: products){
			BigDecimal total = calculateTotalForProduct.forProduct(productName);
			log.info("total sales for " + productName +  " = $" + total);
		}
	}
}
