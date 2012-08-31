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
package org.springframework.data.gemfire.examples.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;

@SuppressWarnings("rawtypes")
/**
 * A simple CacheListener to log cache events
 * @author David Turanski
 *
 */
@Component
public class LoggingCacheListener extends CacheListenerAdapter {
	private static Log log = LogFactory.getLog(LoggingCacheListener.class);

	 
	@Override
	  public void afterCreate(EntryEvent event) {
	    final String regionName = event.getRegion().getName();
	    final Object key = event.getKey();
	    final Object newValue = event.getNewValue();
	    log.info("In region [" + regionName + "] created key [" + key
	        + "] value [" + newValue + "]");
	  }

	  @Override
	  public void afterDestroy(EntryEvent event) {
	    final String regionName = event.getRegion().getName();
	    final Object key = event.getKey();
	    log.info("In region [" + regionName + "] destroyed key [" + key
	        + "]");
	  }

	  @Override
	  public void afterUpdate(EntryEvent event) {
	    final String regionName = event.getRegion().getName();
	    final Object key = event.getKey();
	    final Object newValue = event.getNewValue();
	    final Object oldValue = event.getOldValue();
	    log.info("In region [" + regionName + "] updated key [" + key
	        + "] [oldValue [" + oldValue + "] new value [" + newValue +"]");
	  }
}
