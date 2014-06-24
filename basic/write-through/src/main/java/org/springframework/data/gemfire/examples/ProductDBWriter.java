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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.examples.repository.ProductRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.CacheWriter;
import com.gemstone.gemfire.cache.CacheWriterException;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.RegionEvent;

/**
 * A cache writer that synchronizes a backing store using a Spring Data repository
 * Methods annotated @ASync will run in a separate thread if the application context
 * enables these annotations
 * 
 * @author David Turanski
 *
 */
@Component
public class ProductDBWriter implements CacheWriter<Long, Product> {
	@Autowired 
	private ProductRepository productRepository;
	
	private static Log log = LogFactory.getLog(ProductDBWriter.class);
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheCallback#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheWriter#beforeCreate(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	@Async
	public void beforeCreate(EntryEvent<Long, Product> entryEvent) throws CacheWriterException {
		if (productRepository.findOne(Long.parseLong(String.valueOf(entryEvent.getKey()))) == null) {
			update(entryEvent.getNewValue());
		}
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheWriter#beforeDestroy(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	@Async
	public void beforeDestroy(EntryEvent<Long, Product> entryEvent) throws CacheWriterException {
		delete(Long.parseLong(String.valueOf(entryEvent.getKey())));
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheWriter#beforeRegionClear(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void beforeRegionClear(RegionEvent<Long, Product> entryEvent) throws CacheWriterException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheWriter#beforeRegionDestroy(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void beforeRegionDestroy(RegionEvent<Long, Product> regionEvent)
			throws CacheWriterException {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheWriter#beforeUpdate(com.gemstone.gemfire.cache.EntryEvent)
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
		productRepository.delete(id);
	}
}
