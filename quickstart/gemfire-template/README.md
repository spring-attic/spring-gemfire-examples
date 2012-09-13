Spring GemfireTemplate Example
==============================

This demonstrates Spring Framework's GemfireTemplate to access the Gemfire cache.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-gemfire-template

Or to run from your IDE, run the following command once for all projects.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run Main.java

#Details

This example demonstrates the use of GemfireTemplate to perform region data access. GemfireTemplate wraps the native
GemFire Region and translates GemFire checked exceptions and native runtime exceptions to Spring's DataAccessException
RuntimeException hierarchy. This plays well with Spring's [declarative transaction management]
(http://static.springsource.org/spring/docs/current/spring-framework-reference/htmlsingle/spring-framework-reference.html#transaction). 
The CustomerService class includes a deleteCustomer(...) method which deletes a Customer from the Customer Region and also deletes any of that 
customer's orders from the Order region. This is an atomic unit of work, enclosed in a transaction by the 
[GemfireTransactionManager](http://static.springsource.org/spring-gemfire/docs/current/reference/html/apis.html#apis:tx-mgmt) 
using @Transactional. 

