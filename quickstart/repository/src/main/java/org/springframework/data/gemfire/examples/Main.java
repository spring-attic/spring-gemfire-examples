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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	private static Log log = LogFactory.getLog(Main.class);
    private static boolean usePdx;
	public static void main(String args[]) {
		
		usePdx = args.length > 0 && args[0].toUpperCase().contains("PDX");  
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		if (usePdx) {
			log.info("PDX is enabled");
			File pdxDir = new File("pdx");
			if (!pdxDir.exists()){
				new File("pdx").mkdir();
			}
			context.getEnvironment().setActiveProfiles("pdx");
		}
	 
		context.scan("org.springframework.data.gemfire.examples");
		context.refresh();
		
		 OrderExample orderExample = context.getBean(OrderExample.class);
		 orderExample.run();
	} 
}
