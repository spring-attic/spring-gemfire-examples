Spring Data GemFire Examples
============================

This project provides a number of examples to get you started using Spring Data GemFire. These examples are designed to work with [Spring Data GemFire] (http://www.springsource.org/spring-gemfire) 1.2 or higher and are organized into the following sub projects:

# Quickstart

These examples show case the application programming model provided by Spring Data GemFire and are not concerned so much with configuration of GemFire components such as Cache and Region. This currently includes:

* spring-cache - Using Spring's Cache abstraction with GemFire
* repository - Using [Spring Data](http://www.springsource.org/spring-gemfire) Repositories with GemFire
* gemfire-template - Using GemfireTemplate to simplify and enhance accessing Region data
* cq - Configuring and using GemFire Continuous Queries

# Basic

These examples are focused more on configuring GemFire components such as Caches and Regions to address various scenarios. This currently includes

* replicated - A simple demonstration of using a replicated region in a peer-to-peer configuration
* replicated-cs - Similar to the above with a client-server configuration
* partitioned - Demonstrates the use of a partitioned region and a custom PartitionResolver
* persistence - Demonstrates the use of persistent backup and disk overflow
* transaction - Demonstrates the use of GemFire transactions
* write-through - Demonstrates synchronous updates to a database
* write-behind - Demonstrates asynchronous updates to a database
* function - Demonstrates the use of GemFire function execution

# Advanced

These examples demonstrate additional GemFire features and require a full installation of GemFire. A trial version may be obtained [here](http://www.vmware.com/products/application-platform/vfabric-gemfire/overview.html).

* gateway - Demonstrates how to use and configure a WAN Gateway
* server-failover - Demonstrates how GemFire handles server down situations
* locator-failover - Demonstrates how GemFire handles locator down situations
