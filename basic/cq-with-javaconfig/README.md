GemFire Continuous Query (CQ) Configured with Spring JavaConfig
===============================================================

This example demonstrates GemFire's [Continuous Query (CQ)](https://gemfire.docs.pivotal.io/geode/developing/continuous_querying/chapter_overview.html) functionality.
Specifically, this example shows how to use _Spring's_ [Java-based Container Configuration](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java)
along with _Spring Data GemFire's_ [ContinuousQueryListenerContainer](https://docs.spring.io/spring-data-gemfire/docs/current/reference/html/#apis:cq-container)
declared in JavaConfig to define cache client _Continuous Queries_ (CQ),
register for notifications and process CQ events.

This examples uses GemFire's [client/server](https://gemfire.docs.pivotal.io/geode/topologies_and_comm/cs_configuration/chapter_overview.html)
topology.

The `Server` class in this example is a _Spring Boot_ application that bootstraps
an embedded GemFire Server (peer cache member of the GemFire cluster/distributed system)
and starts a GemFire `CacheServer` allowing cache clients to connect.
In addition, it also creates a `/Orders` _Region_ in which `Orders`
will be placed and CQ events generated.

The application simulates `Orders` from `Customers` using _Spring's_ [scheduling infrastructure](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#scheduling),
placing `Orders` into the `/Orders` _Region_.

The `Client` class in this example is also a _Spring Boot_ application
as well as a GemFire cache client.  It connects to the GemFire `CacheServer` directly
using the default `CacheServer's` host and port (i.e. 40404).

The client registers 2 CQs, 1 to capture events for `Orders` under $1000
and another for `Orders` over $2000.  Both the client and server log
the entries in the `/Orders` _Region_.  The `Server` does so with
a `CacheListener` and the client does so using a SDG `ContinuousQueryListener`.

To run this example, open a command window, go to the the `spring-gemfire-examples` root directory, and type:

        ./gradlew -q run-cq-with-javaconfig -Pmain=server.Server

In another window, type:

        ./gradlew -q run-cq-with-javaconfig -Pmain=client.Client

Or, to run from your IDE, execute one of the following tasks once:

        ./gradlew eclipse
        ./gradlew idea

Then import the project into your IDE and run the above classes.

# Additional Details

An implementation of SDG's `ContinuousQueryListener` is a POJO (albeit dependent only on the GemFire type `CQEvent`).
It is not required to implement any specific interfaces, but it must provide appropriate method signatures
so the `ConitnuousQueryListenerContainer` can delegate `CQEvents` to it.

Additionally, the cache client, _Spring Boot_ application's `Pool`
must be configured with `subscription-enabled='true'`.
