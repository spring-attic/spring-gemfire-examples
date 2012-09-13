Spring Data Gemfire Transaction Example
=======================================

This example demonstrates how to use Spring's declarative transaction management using the GemfireTransactionManager and Spring Data Gemfire Repositories. The example creates 
a customer in the Customer region and then performs an update which throws an exception after the cache entry is updated, triggering a rollback. 

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

		./gradlew -q run-transaction

Or to run from your IDE, run one of the following commands once.

		./gradlew eclipse
		./gradlew idea 

Then import the project into your IDE and run Main.java

# Details

The CustomerService uses CustomerRepository to perform CRUD operations on the Customer region. The CustomerService.updateCustomer() method is annoted with 
@Transactional to wrap each method invocation in a GemFire transaction. The method has a boolean argument that, if set to true, will throw an exception after 
the repository operation completes. This simulates an exception that could occur during some subsequent step that must be performed within the same transaction.

The application configuration uses Java configuration. The configuration class is ApplicationConfig.java.
