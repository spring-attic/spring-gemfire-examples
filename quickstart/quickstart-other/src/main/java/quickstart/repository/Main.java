/*
 * Copyright 2010-2012 the original author or authors.
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
package quickstart.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.Region;

/**
 * @author David Turanski
 * 
 */
public class Main {
	private static Log log = LogFactory.getLog(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/quickstart/repository/app-config.xml");
		CustomerClient customerClient = context.getBean(CustomerClient.class);
		customerClient.start();
		Region customers = context.getBean("customers", Region.class);
		log.debug(customers.keySet().size());
		log.debug(customers.get(1));
		log.debug(customers.isEmpty());
	}

}
