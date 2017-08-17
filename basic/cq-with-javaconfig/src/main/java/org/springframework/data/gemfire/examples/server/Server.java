/*
 *  Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.gemfire.examples.server;

import static org.springframework.data.gemfire.util.ArrayUtils.asArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.examples.domain.Address;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;
import org.springframework.data.gemfire.examples.domain.LineItem;
import org.springframework.data.gemfire.examples.domain.Order;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.cache.util.CacheListenerAdapter;

/**
 * The {@link Server} class is a Spring Boot application with an embedded GemFire peer cache
 * running a GemFire {@link CacheServer} for cache clients simulating placed {@link Order orders}
 * from {@link Customer customers} at a scheduled, fixed interval.
 *
 * This Spring Boot, GemFire (Cache) Server application enables GemFire cache clients to connect, register CQs
 * and receive CQ events (notifications) of when new {@link Order orders} are placed.
 *
 * @author John Blum
 * @see org.apache.geode.cache.CacheListener
 * @see org.apache.geode.cache.GemFireCache
 * @see org.apache.geode.cache.Region
 * @see org.apache.geode.cache.server.CacheServer
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheApplication
 * @see org.springframework.data.gemfire.PartitionedRegionFactoryBean
 * @see org.springframework.data.gemfire.RegionLookupFactoryBean
 * @see org.springframework.data.gemfire.examples.domain.Address
 * @see org.springframework.data.gemfire.examples.domain.Customer
 * @see org.springframework.data.gemfire.examples.domain.EmailAddress
 * @see org.springframework.data.gemfire.examples.domain.LineItem
 * @see org.springframework.data.gemfire.examples.domain.Order
 * @see org.springframework.data.gemfire.examples.domain.Product
 * @see org.springframework.scheduling.annotation.EnableScheduling
 * @see org.springframework.scheduling.annotation.Scheduled
 * @since 2.0.0
 */
@SpringBootApplication
@EnableScheduling
@CacheServerApplication(name = "GemFireContinuousQueryServer")
@SuppressWarnings("unused")
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	@Bean(name = "Customers")
	PartitionedRegionFactoryBean<Long, Customer> customersRegion(GemFireCache gemfireCache) {

		PartitionedRegionFactoryBean<Long, Customer> customers = new PartitionedRegionFactoryBean<>();

		customers.setCache(gemfireCache);
		customers.setClose(false);
		customers.setPersistent(false);

		return customers;
	}

	@Bean(name = "Orders")
	PartitionedRegionFactoryBean<Long, Order> ordersRegions(GemFireCache gemfireCache) {

		PartitionedRegionFactoryBean<Long, Order> orders = new PartitionedRegionFactoryBean<>();

		orders.setCache(gemfireCache);
		orders.setCacheListeners(asArray(orderCacheListener()));
		orders.setClose(false);
		orders.setPersistent(false);

		return orders;
	}

	@Bean(name = "Products")
	ReplicatedRegionFactoryBean<Long, Product> productsRegion(GemFireCache gemfireCache) {

		ReplicatedRegionFactoryBean<Long, Product> products = new ReplicatedRegionFactoryBean<>();

		products.setCache(gemfireCache);
		products.setClose(false);
		products.setPersistent(false);

		return products;
	}

	private CacheListener<Long, Order> orderCacheListener() {

		return new CacheListenerAdapter<Long, Order>() {

			@Override
			public void afterCreate(EntryEvent<Long, Order> event) {

				Order order = event.getNewValue();

				System.err.printf("[%s] made an Order [%s] for [%s]%n",
					findCustomerById(order.getCustomerId()), order, order.getLineItems());
			}
		};
	}

	private static final AtomicLong id = new AtomicLong(0L);

	private static final List<Customer> customerList = new ArrayList<>();

	private static final Customer jonDoe = newCustomer("Jon", "Doe");
	private static final Customer janeDoe = newCustomer("Jane", "Doe");
	private static final Customer cookieDoe = newCustomer("Cookie", "Doe");
	private static final Customer froDoe = newCustomer("Fro", "Doe");
	private static final Customer hoDoe = newCustomer("Ho", "Doe");
	private static final Customer lanDoe = newCustomer("Lan", "Doe");
	private static final Customer pieDoe = newCustomer("Pie", "Doe");
	private static final Customer sourDoe = newCustomer("Sour", "Doe");
	private static final Customer jackHandy = newCustomer("Jack", "Handy");

	private static final List<Product> productList = new ArrayList<>();

	private static final Product appleIPad = newProduct("Apple iPad Pro", newPrice(999));
	private static final Product appleIPhone = newProduct("Apple iPhone 8", newPrice(899));
	private static final Product appleMacBookPro = newProduct("Apple MacBook Pro", newPrice(2799));
	private static final Product golfBalls = newProduct("Titleist Pro V1x", newPrice(36));
	private static final Product golfClubs = newProduct("Mizuno MP5", newPrice(1199));
	private static final Product golfPutter = newProduct("Harry Putter", newPrice(150));
	private static final Product golfRange = newProduct("Large Bucket of Golf Balls", newPrice(10));
	private static final Product golfRound = newProduct("Bandon Dunes Golf Course", newPrice(250));
	private static final Product bike = newProduct("Trek Madonne 4300", newPrice(3000));
	private static final Product playStationFour = newProduct("Play Station 4", newPrice(300));
	private static final Product sonyBraviaTv = newProduct("Sony Bravia 55 4K Ultra HDTV", newPrice(2000));

	private static Customer findCustomer(int index) {
		return customerList.get(index);
	}

	private static Customer findCustomerById(Long id) {
		return customerList.stream().filter(customer -> customer.getId().equals(id)).findAny().orElse(null);
	}

	private static Product findProduct(int index) {
		return productList.get(index);
	}

	private static Customer newCustomer(String firstName, String lastName) {

		Customer customer = new Customer(id.incrementAndGet(),
			new EmailAddress(String.format("%1$s.%2$s@home.com", firstName, lastName)), firstName, lastName);

		customer.add(new Address("100 Main St.", "Portland", "USA"));

		customerList.add(customer);

		return customer;
	}

	private static LineItem newLineItem(Product product, int amount) {
		return new LineItem(product, amount);
	}

	private static Order newOrder(Customer customer) {
		return new Order(id.incrementAndGet(), customer.getId(), customer.getAddresses().iterator().next());
	}

	private static BigDecimal newPrice(int value) {
		return new BigDecimal(value);
	}

	private static Product newProduct(String name, BigDecimal price) {

		Product product = new Product(id.incrementAndGet(), name, price);

		productList.add(product);

		return product;
	}

	@Resource(name = "Customers")
	private Region<Long, Customer> customers;

	@Resource(name = "Orders")
	private Region<Long, Order> orders;

	@Resource(name = "Products")
	private Region<Long, Product> products;

	private Random random = new Random(System.currentTimeMillis());

	@Scheduled(initialDelay = 2000L, fixedRate = 5000L)
	public void fulfillOrder() {

		int customerIndex = random.nextInt(customerList.size());

		Customer customer = save(findCustomer(customerIndex));

		Order order = newOrder(customer);

		for (int count = Math.max(1, random.nextInt(6)); count > 0; count--) {

			int productIndex = random.nextInt(productList.size());

			Product product = save(findProduct(productIndex));

			order.add(newLineItem(product, Math.max(1, random.nextInt(3))));
		}

		orders.put(order.getId(), order);
	}

	private Customer save(Customer customer) {
		this.customers.put(customer.getId(), customer);
		return customer;
	}

	private Product save(Product product) {
		this.products.put(product.getId(), product);
		return product;
	}
}
