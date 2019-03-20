Spring Data For Pivotal GemFire and Apache Geode Examples
=========================================================

This project provides a number of examples to get you started using Spring Data for Apache Geode or Pivotal GemFire.
These examples are designed to work with [Spring Data for Pivotal GemFire] (https://projects.spring.io/spring-data-gemfire) 1.2
or higher and are organized into the following sub projects:

> NOTE: [Apache Geode](https://geode.apache.org/) is the open source core
of [Pivotal GemFire](https://pivotal.io/pivotal-gemfire).

> NOTE: Where ever Pivotal GemFire is referenced, this equally applies to Apache Geode
and where ever Spring Data for Pivotal GemFire is referenced, this equally applies to
Spring Data for Apache Geode.

# Quickstart

These examples show case the application programming model provided by Spring Data for Pivotal GemFire
and are not concerned as much with of configuration of Apache Geode or Pivotal GemFire components
such as Cache and Region.

The Quickstart examples currently include:

* spring-cache - Using Spring's Cache abstraction with Pivotal GemFire
* repository - Using [Spring Data](https://projects.spring.io/spring-data) Repositories with Pivotal GemFire
* gemfire-template - Using [GemfireTemplate](https://docs.spring.io/spring-data/geode/docs/current/api/org/springframework/data/gemfire/GemfireTemplate.html) to simplify and enhance accessing Region data
* cq - Configuring and using Pivotal GemFire Continuous Queries
* transaction - Demonstrates the use of Pivotal GemFire transactions

# Basic

These examples are focused more on configuring Apache Geode or Pivotal GemFire components
such as Caches and Regions to address various scenarios.

The Basic examples currently include:

* replicated - A simple demonstration of using a REPLICATE Region in a peer-to-peer configuration
* replicated-cs - Similar to the above with a client-server configuration
* partitioned - Demonstrates the use of a PARTITION Region and a custom PartitionResolver
* persistence - Demonstrates the use of persistent backup and disk overflow
* write-through - Demonstrates loading data from and executing synchronous (write-through) or asynchronous (write-behind) updates to a database*
* function - Demonstrates the use of Pivotal GemFire function execution
* java-config - Demonstrates how to configure a Pivotal GemFire Server (data node)
using Spring's Java-based Container Configuration and Spring Data for Pivotal GemFire

# Advanced

These examples demonstrate additional Apache Geode or Pivotal GemFire features
and require a full installation of either Apache Geode or Pivotal GemFire.

You can acquire Apache Geode bits from [here](https://geode.apache.org/releases/).

You can download a trial version of Pivotal GemFire from [here](https://pivotal.io/pivotal-gemfire).

* gateway - Demonstrates how to use and configure a WAN Gateway
* locator-failover - Demonstrates how Pivotal GemFire handles Locator down situations

# Running The Examples

This project is built with Gradle and each example may be run with Gradle or within your Java IDE.
If you are using Eclipse or Spring Tool Suite, go to the directory where you downloaded this project
and type:

        ./gradlew eclipse

If you are using IntelliJ IDEA,

        ./gradlew idea

Detailed instructions for each example may be found in its own README file.

# Running a cache server with custom configuration

As a convenience, this project includes [GenericServer.java](https://github.com/spring-projects/spring-gemfire-examples/blob/master/spring-gemfire-examples-common/src/main/java/org/springframework/data/gemfire/examples/GenericServer.java)
used to start a cache server with a custom Spring configuration. Simply point to a valid Spring configuration on the file system using the built in task:

	./gradlew -q run-generic-server -Pargs=path-to-spring-config-xml-file

This is useful for testing or experimentation with client/server scenarios.
If your application requires additional jars to be deployed to the server, you can create a lib directory under the project root (e.g., spring-gemfire-examples) and drop them in there.
The gradle build is already configured to look there.

Note, this is a 'quick and dirty' way to do this. In a shared integration or production environment, you should use the Pivotal GemFire Shell program, _gfsh_.














