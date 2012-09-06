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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.examples.repository.ProductRepository;

import com.gemstone.gemfire.cache.Region;

/**
 * @author David Turanski
 *
 */
public class Main {
	
	private static Log log = LogFactory.getLog(Main.class);
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		
		if (args.length >= 1) {
			log.debug("Setting the spring profile to " + args[0]);
			System.setProperty("spring.profiles.active", args[0]);
		}
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"app-context.xml");
		
		Region<Long,Product> region = context.getBean(Region.class);
		ProductRepository productRepository = context.getBean(ProductRepository.class);
		
		for (long i = 1; i<=3; i++){
			Product p = region.get(i);
			log.debug("Retrieved product " + p.getName() + " from cache");
		}
		
		 //Let's try this again
		
		log.debug("2nd pass...This time the CacheLoader is not called since items are already cached");
		for (long i = 1; i<=3; i++){
			Product p = region.get(i);
			log.debug("Retrieved product " + p.getName() + " from cache");
		}		
		
		//Create a new product
		log.debug("Adding a new product to the cache...");
		
		Product iphone = new Product(4L, "Apple IPhone", new BigDecimal(299.99),"Smart phone");
		region.put(iphone.getId(), iphone);
		
		while (productRepository.count() == 3) {
			log.debug("Product repository still has : " + productRepository.count() + " rows");
			Thread.sleep(10);
		}
		log.debug("Product repository now has : " + productRepository.count() + " rows");
		
		log.debug("Removing a product from the cache...");
		region.destroy(4L);
		
		while (productRepository.count() == 4) {
			log.debug("Product repository still has : " + productRepository.count() + " rows");
			Thread.sleep(10);
		}
		log.debug("Product repository now has : " + productRepository.count() + " rows");
	}

}
