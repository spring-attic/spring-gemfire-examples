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

import java.io.File;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Order;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.util.GatewayHub;
import com.gemstone.gemfire.cache.wan.GatewayReceiver;

/**
 * @author David Turanski
 * 
 */
public class GatewayExample {
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void run(String location, long id) {  
		
		File diskStoreDirectory = new File(location);
		if (!diskStoreDirectory.exists()){
			diskStoreDirectory.mkdir();
		}
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				location + "/cache-config.xml");
		Cache cache = context.getBean(Cache.class);
		GatewayReceiver receiver = cache.getGatewayReceivers().iterator().next();
		if (receiver.isRunning()){
			System.out.println("gateway reveiver is running on " +receiver.getPort());
		} else {
			System.out.println("gateway receiver is not started");
		}
		
		Region<Long, Order> region = context.getBean(Region.class);
		
		try {
			System.out.println("Press <Enter> to update region " + region.getName());
			System.in.read();
			region.put(id, new Order(1L, 1L, new Address("street", "city",
					"country")));
			System.out.println("Press <Enter> to quit");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
