/*
 * Copyright 2010 the original author or authors.
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

package quickstart;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import quickstart.ClientConsumer.RegisterInterestType;

public class ClientConsumerApp {

	public static final String USAGE = "Usage: java ClientConsumerApp <register-interest-type>\n"
			+ "  register-interest-type may be one of the following:\n"
			+ "    all-keys    Register interest in all keys on the server\n"
			+ "    keyset      Register interest in a set of keys on the server\n"
			+ "    regex       Register interest in keys on the server matching a regular expression\n";

	private static final String[] CONFIGS = new String[] { "client-consumer-app-context.xml" };

	/**
	 * Client Consumer Application startup class. Bootstraps the Spring
	 * container which in turns starts GemFire and the actual application.
	 * <p/>
	 * Accepts as optional parameters location of one (or multiple) application
	 * contexts that will be used for configuring the Spring container. See the
	 * reference documentation for more {@link http
	 * ://static.springsource.org/spring
	 * /docs/3.0.x/spring-framework-reference/html/resources.html information}.
	 * 
	 * Note that in most (if not all) managed environments writing such a class
	 * is not needed as Spring already provides the required integration.
	 * 
	 * @see org.springframework.web.context.ContextLoaderListener
	 * @author David Roberts
	 */

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println(USAGE);
			System.exit(1);
		}

		ClientConsumer.RegisterInterestType registerInterestType;
		if (args[0].equals("all-keys")) {
			registerInterestType = RegisterInterestType.ALL_KEYS;
		}
		else if (args[0].equals("keyset")) {
			registerInterestType = RegisterInterestType.KEYSET;
		}
		else if (args[0].equals("regex")) {
			registerInterestType = RegisterInterestType.REGEX;
		}
		else {
			registerInterestType = null;
			System.out.println(USAGE);
			System.exit(2);
		}

		String[] res = CONFIGS;
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(res);
		// shutdown the context along with the VM
		ctx.registerShutdownHook();
		ClientConsumer bean = ctx.getBean(ClientConsumer.class);
		try {
			bean.consume(registerInterestType);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
