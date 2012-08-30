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

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.examples.util.ServerPortGenerator;

public class Server {
	 public static void main(String args[]) throws IOException {
		
		/*
		 *  Check if port is open. Currently the client pool is hard coded to look for a server on 40404, the default. If already taken, 
		 *  this process will wait for a while so this forces an immediate exit if the port is in use. There are better ways to handle this 
		 *  situation, but hey, this is sample code.   
		 */
		try {
			new ServerPortGenerator().bind(new ServerSocket(), 40404,1);
		} catch (IOException e) {
			System.out.println("Sorry port 40404 is in use. Do you have another cache server process already running?");
			System.exit(1);
			
		}
		
		new ClassPathXmlApplicationContext("server/cache-config.xml");
		
		System.out.println("Press <Enter> to terminate the server");
		System.in.read();
		System.exit(0);
	}

	
}
