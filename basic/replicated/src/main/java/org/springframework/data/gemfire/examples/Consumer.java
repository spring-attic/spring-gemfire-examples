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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Customer;

import com.gemstone.gemfire.cache.Region;

public class Consumer {
	 private static Log log = LogFactory.getLog(Consumer.class);

	 
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("consumer/cache-config.xml");
		Region<Long,Customer> region = context.getBean(Region.class);
		
		System.out.println("Please Start the Producer. Then press <Enter>");
		System.in.read();
		
		Customer dave = region.get(1L);
		if (dave != null) {
			log.debug("retrieved " + dave.getFirstname() + " " + dave.getLastname());
		}
		
		Customer alicia = region.get(2L);
		if (alicia !=null) {
			log.debug("retrieved " + alicia.getFirstname() + " " + alicia.getLastname());
			alicia.add(new Address("Keys Street","Alicia","UK"));
			region.put(alicia.getId(), alicia);
		}
	}

	
}
