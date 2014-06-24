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
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.LoaderHelper;

/**
 * A cache loader that loads Product entries from a backing store using a Spring Data Repository
 * @author David Turanski
 *
 */
@Component
public class ProductDBLoader implements CacheLoader<Long, Product> {
	@Autowired 
	private ProductRepository productRepository;
	
	private static Log log = LogFactory.getLog(ProductDBLoader.class);
	
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheCallback#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheLoader#load(com.gemstone.gemfire.cache.LoaderHelper)
	 */
	@Override
	public Product load(LoaderHelper<Long, Product> loadHelper) throws CacheLoaderException {
		Long id = Long.parseLong(String.valueOf(loadHelper.getKey()));
		log.debug("loading product id " + id + " from the database");
		return productRepository.findOne(id);
	}
}
