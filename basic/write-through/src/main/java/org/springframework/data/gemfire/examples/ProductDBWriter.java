/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.gemfire.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.examples.repository.ProductRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geode.cache.CacheWriter;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

/**
 * A cache writer that synchronizes a backing store using a Spring Data repository
 * Methods annotated @ASync will run in a separate thread if the application context
 * enables these annotations
 *
 * @author David Turanski
 */
@Component
public class ProductDBWriter implements CacheWriter<Long, Product> {

	@Autowired
	private ProductRepository productRepository;

	private static Log log = LogFactory.getLog(ProductDBWriter.class);
	/* (non-Javadoc)
	 * @see org.apache.geode.cache.CacheCallback#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.geode.cache.CacheWriter#beforeCreate(org.apache.geode.cache.EntryEvent)
	 */
	@Override
	@Async
	public void beforeCreate(EntryEvent<Long, Product> entryEvent) throws CacheWriterException {
		if (productRepository.findById(Long.parseLong(String.valueOf(entryEvent.getKey()))).isPresent()) {
			update(entryEvent.getNewValue());
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.geode.cache.CacheWriter#beforeDestroy(org.apache.geode.cache.EntryEvent)
	 */
	@Override
	@Async
	public void beforeDestroy(EntryEvent<Long, Product> entryEvent) throws CacheWriterException {
		delete(Long.parseLong(String.valueOf(entryEvent.getKey())));
	}

	/* (non-Javadoc)
	 * @see org.apache.geode.cache.CacheWriter#beforeRegionClear(org.apache.geode.cache.RegionEvent)
	 */
	@Override
	public void beforeRegionClear(RegionEvent<Long, Product> entryEvent) throws CacheWriterException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.geode.cache.CacheWriter#beforeRegionDestroy(org.apache.geode.cache.RegionEvent)
	 */
	@Override
	public void beforeRegionDestroy(RegionEvent<Long, Product> regionEvent)
			throws CacheWriterException {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.apache.geode.cache.CacheWriter#beforeUpdate(org.apache.geode.cache.EntryEvent)
	 */
	@Override
	@Async
	public void beforeUpdate(EntryEvent<Long, Product> entryEvent) throws CacheWriterException {
		update(entryEvent.getNewValue());
	}

	private void update(Product product) {
		log.debug("updating the database");
		productRepository.save(product);
	}

	private void delete(Long id) {
		log.debug("deleting id " + id + " from the database");
		productRepository.deleteById(id);
	}
}
