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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;

import com.gemstone.gemfire.cache.Region;

public class Client {

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException {
		
		System.out.println("Start locators on ports 10334 and 10335 and the Server. Press <Enter> to continue");
		System.in.read();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("client/cache-config.xml");
		Region<Long,Customer> region = context.getBean(Region.class);
	
		
		Customer dave = new Customer(1L,new EmailAddress("dave@matthews.com"),"Dave","Matthews");
		Customer alicia = new Customer(2L,new EmailAddress("alicia@keys.com"),"Alicia","Keys");
		region.put(dave.getId(),dave);
		region.put(alicia.getId(),alicia);
		
		System.out.println("Stop the primary locator on port 10334. Press <Enter> to continue");
		System.in.read();
		
		Customer john = new Customer(3L,new EmailAddress("john@mayer.com"),"John","Mayer");
		Customer gwen = new Customer(4L,new EmailAddress("gwen@stefani.com"),"Gwen","Stefani");
		region.put(john.getId(),john);
		region.put(gwen.getId(),gwen);
		
		System.exit(0); 
	}
}
