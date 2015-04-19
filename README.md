Spring Data GemFire Examples
============================

This project provides a number of examples to get you started using Spring Data GemFire. These examples are designed to work with [Spring Data GemFire] (http://projects.spring.io/spring-data-gemfire) 1.2 or higher and are organized into the following sub projects:

# Quickstart

These examples show case the application programming model provided by Spring Data GemFire and are not concerned so much with configuration of GemFire components such as Cache and Region. This currently includes:

* spring-cache - Using Spring's Cache abstraction with GemFire
* repository - Using [Spring Data](http://projects.spring.io/spring-data) Repositories with GemFire
* gemfire-template - Using GemfireTemplate to simplify and enhance accessing Region data
* cq - Configuring and using GemFire Continuous Queries
* transaction - Demonstrates the use of GemFire transactions

# Basic

These examples are focused more on configuring GemFire components such as Caches and Regions to address various scenarios. This currently includes

* replicated - A simple demonstration of using a replicated region in a peer-to-peer configuration
* replicated-cs - Similar to the above with a client-server configuration
* partitioned - Demonstrates the use of a partitioned region and a custom PartitionResolver
* persistence - Demonstrates the use of persistent backup and disk overflow
* write-through - Demonstrates loading data from and executing synchronous (write-through) or asynchronous(write-behind) updates to a database* 
* function - Demonstrates the use of GemFire function execution
* java-config - Demonstrates how to configure a GemFire Server (data node) using Spring Java-based Container Configuration and Spring Data GemFire

# Advanced

These examples demonstrate additional GemFire features and require a full installation of GemFire. A trial version may be obtained [here](http://www.pivotal.io/big-data/pivotal-gemfire).

* gateway - Demonstrates how to use and configure a WAN Gateway
* locator-failover - Demonstrates how GemFire handles locator down situations

# Running The Examples

This project is built with gradle and each example may be run with gradle or within your Java IDE. If you are using Eclipse or SpringSource Tool Suite, go to the directory where you downloaded this project and type:

        ./gradlew eclipse

If you are using IDEA, 

        ./gradlew idea

Detailed instructions for each example may be found in its own README file.

# Running a cache server with custom configuration

As a convenience, this project includes [GenericServer.java] (https://github.com/spring-projects/spring-gemfire-examples/blob/master/spring-gemfire-examples-common/src/main/java/org/springframework/data/gemfire/examples/GenericServer.java)
used to start a cache server with a custom spring configuration. Simply point to a valid spring configuration on the file system using the built in task:

	./gradlew -q run-generic-server -Pargs=path-to-spring-config-xml-file

This is useful for testing or experimentation with client server scenarios. 
If your application requires additional jars to be deployed to the server, you can create a lib directory under the project root (e.g., spring-gemfire-examples) and drop them in there. The gradle build is already configured to look there. Note, 
this is a 'quick and dirty' way to do this. In a shared integration or production environment, you should use The GemFire shell program, gfsh.
																																									





	
  





