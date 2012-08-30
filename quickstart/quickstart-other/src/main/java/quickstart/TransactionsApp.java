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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TransactionsApp {

	private static final String[] CONFIGS = new String[] { "transactions-app-context.xml" };

	/**
	 * Transactions Application startup class. Bootstraps the Spring container
	 * which in turns starts GemFire and the actual application.
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

		try {
			String[] res = (args != null && args.length > 0 ? args : CONFIGS);
			AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
					res);
			// shutdown the context along with the VM
			ctx.registerShutdownHook();
			Transactions bean = ctx.getBean(Transactions.class);

			bean.printRegionData();

			// Begin transaction that I will commit...
			System.out
					.println("\nBefore the first transaction, the cache is empty.\n");

			bean.populateRegionAndCommit();

			System.out
					.println("\nAfter committing the first transaction, the cache contains:");
			bean.printRegionData();

			System.out
					.println("\nPress Enter to continue to next transaction...");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(System.in));
			bufferedReader.readLine();

			System.out
					.println("Before the second transaction, the cache contains:");
			bean.printRegionData();
			System.out.println("");

			try {
				bean.populateAndRollback();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out
					.println("\nAfter rolling back the second transaction, the cache contains:");
			bean.printRegionData();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
