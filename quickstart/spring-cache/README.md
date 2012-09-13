Spring Cache Example
====================

This demonstrates Spring Framework's @Cacheable annotation backed by GemFire.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-spring-cache

Or to run from your IDE, execute one of the following tasks once.

         ./gradlew eclipse
         ./gradlew idea 

Then import the project into your IDE and run Main.java

# Details
The CustomerService uses a CustomerDao to retrieve customer data. To improve performance, customer data is cached so if the same customer is
retrieved again the cached instance will be returned.

The method CustomerService.findCustomer() is annotated with @Cacheable("Customer"), which refers to a GemFire Region named 'Customer'. 
The GemFire cache and region are configured in cache-context.xml and the cacheManager are Spring beans configured in app-context.xml

See [This section of the Spring Reference](http://static.springsource.org/spring/docs/current/spring-framework-reference/htmlsingle/spring-framework-reference.html#new-in-3.1-cache-abstraction) 
for more information.

