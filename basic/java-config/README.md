GemFire Configuration Using Spring Java-based Container Configuration
=====================================================================

This example demonstrates how to configure a GemFire Server (data node) using Spring's [Java-based Container Configuration] (http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java)
along with Spring Data GemFire's custom [GemFire FactoryBean components and classes] (https://github.com/spring-projects/spring-data-gemfire/tree/master/src/main/java/org/springframework/data/gemfire).

After reviewing this example, a developer should gain a general understanding of how to configure a single GemFire Server
(data node) instance using Spring's Java-based Container Configuration and Spring Data GemFire.  The @Configuration class
declares bean definitions for the GemFire Cache and creates a PARTITION Region with basic properties, such as Eviction
and entry TTI (Idle-Timeout) and TTL (Time-To-Live) Region settings, along with PARTITION attributes for setting the
local and total max memory used by the Region as well as it's redundancy.

The example primarily consists of a Spring Java-based Container Configuration class, [SpringJavaBasedContainerGemFireConfiguration] (https://github.com/spring-projects/spring-gemfire-examples/blob/master/basic/java-config/src/main/java/org/springframework/data/gemfire/example/SpringJavaBasedContainerGemFireConfiguration.java)
and a corresponding test suite class, [SpringJavaBasedContainerGemFireConfigurationTest] (https://github.com/spring-projects/spring-gemfire-examples/blob/master/basic/java-config/src/test/java/org/springframework/data/gemfire/example/SpringJavaBasedContainerGemFireConfigurationTest.java)
to verify the proper configuration of the GemFire Server (data node) using JavaConfig.

To run this example, execute the following...

    $gradlew :basic:java-config:test

You can then open the test report file to review the results...

    file:///<absolute-path-to>/spring-gemfire-examples/basic/java-config/build/reports/tests/packages/org.springframework.data.gemfire.example.html

Make sure to substitute "<absolute-path-to>" with the absolute file system path of your cloned copy of the
*spring-gemfire-examples* project.
