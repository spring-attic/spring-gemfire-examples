/*
 *  Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.gemfire.examples.client;

import static org.springframework.data.gemfire.util.CollectionUtils.asSet;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.listener.ContinuousQueryDefinition;
import org.springframework.data.gemfire.listener.ContinuousQueryListener;
import org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;

/**
 * The {@link Client} class is a Spring Boot, GemFire cache client application demonstrating how to
 * register GemFire Continuous Queries (CQ) using Spring Data GemFire's {@link ContinuousQueryListenerContainer}
 * in JavaConfig.
 *
 * @author John Blum
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @see org.springframework.data.gemfire.examples.domain.Customer
 * @see org.springframework.data.gemfire.examples.domain.Order
 * @see org.springframework.data.gemfire.examples.domain.Product
 * @see org.springframework.data.gemfire.listener.ContinuousQueryDefinition
 * @see org.springframework.data.gemfire.listener.ContinuousQueryListener
 * @see org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer
 * @see <a href="http://gemfire.docs.pivotal.io/geode/developing/continuous_querying/chapter_overview.html">Continuous Query</a>
 * @since 2.0.0
 */
@SpringBootApplication
@ClientCacheApplication(name = "GemFireContinuousQueryClient", subscriptionEnabled = true)
@SuppressWarnings("unused")
public class Client {

	public static void main(String[] args) {
		SpringApplication.run(Client.class, args);
		promptForInputToExit();
	}

	private static void promptForInputToExit() {
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}

	@Bean(name = "Customers")
	ClientRegionFactoryBean<Long, Product> customersRegion(GemFireCache gemfireCache) {

		ClientRegionFactoryBean<Long, Product> customers = new ClientRegionFactoryBean<>();

		customers.setCache(gemfireCache);
		customers.setClose(true);
		customers.setShortcut(ClientRegionShortcut.PROXY);

		return customers;
	}

	@Bean(name = "Orders")
	ClientRegionFactoryBean<Long, Product> ordersRegion(GemFireCache gemfireCache) {

		ClientRegionFactoryBean<Long, Product> orders = new ClientRegionFactoryBean<>();

		orders.setCache(gemfireCache);
		orders.setClose(true);
		orders.setShortcut(ClientRegionShortcut.PROXY);

		return orders;
	}

	@Bean(name = "Products")
	ClientRegionFactoryBean<Long, Product> productsRegion(GemFireCache gemfireCache) {

		ClientRegionFactoryBean<Long, Product> products = new ClientRegionFactoryBean<>();

		products.setCache(gemfireCache);
		products.setClose(true);
		products.setShortcut(ClientRegionShortcut.PROXY);

		return products;
	}

	@Bean
	ContinuousQueryListenerContainer continuousQueryListenerContainer(GemFireCache gemfireCache) {

		Region<Long, Customer> customers = gemfireCache.getRegion("/Customers");
		Region<Long, Product> products = gemfireCache.getRegion("/Products");

		ContinuousQueryListenerContainer container = new ContinuousQueryListenerContainer();

		container.setCache(gemfireCache);
		container.setQueryListeners(asSet(cheapOrdersQuery(customers, products, 1000),
			expensiveOrdersQuery(customers, products, 2000)));

		return container;
	}

	private ContinuousQueryDefinition cheapOrdersQuery(
			Region<Long, Customer> customers, Region<Long, Product> products, int total) {

		String query = String.format("SELECT * FROM /Orders o WHERE o.getTotal().intValue() < %d", total);

		return new ContinuousQueryDefinition("Cheap Orders", query,
			newQueryListener(customers, "Cheap"));
	}

	private ContinuousQueryDefinition expensiveOrdersQuery(
			Region<Long, Customer> customers, Region<Long, Product> products, int total) {

		String query = String.format("SELECT * FROM /Orders o WHERE o.getTotal().intValue() > %d", total);

		return new ContinuousQueryDefinition("Expensive Orders", query,
			newQueryListener(customers, "Expensive"));
	}

	private ContinuousQueryListener newQueryListener(Region<Long, Customer> customers, String qualifier) {

		return event -> {

			Order order = (Order) event.getNewValue();

			System.err.printf("[%s] made an %s Order [%s] for [%s]%n",
				findByCustomerId(customers, order.getCustomerId()), qualifier, order, order.getLineItems());
		};
	}

	private Customer findByCustomerId(Region<Long, Customer> customers, Long customerId) {
		return customers.get(customerId);
	}
}
