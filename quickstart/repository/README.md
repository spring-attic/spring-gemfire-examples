Spring Data GemFire Repository Example
=====================================

This example demonstrates how to use Spring Data Gemfire Repositories. The Spring configuration uses environment profiles to 
allow you to run this example with or without PDX serialization enabled.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

		./gradlew -q run-repository

To run with PDX, supply a command line argument, which is done in ./gradlew as follows:

		./gradlew -q run-repository -Pargs=pdx

If pdx is enabled you will see some files created in the 'pdx' folder.

Or to run from your IDE, run one of the following commands once.

		./gradlew eclipse
		./gradlew idea 

Then import the project into your IDE and run Main.java

# Details

The example application code is contained in OrderExample.java.  This class depends on 3 service components, CustomerService, 
ProductService, and OrderService. Each of these is wired with the corresponding GemFire Repository associated to a domain object.
(NOTE: The source code for the domain classes is found in the 'spring-gemfire-examples-common' project). 

For example, CustomerRepository is used to access instances of Customer. Customer is bound to a GemFire Region via the @Region 
annotation. By default the Region name is the same as the simple class name, but may be overridden in the annotation value. 

The Repositories are simply interface definitions. These are discovered using Spring Java config in this case, using the @EnableGemfireRepositories 
annotation. This can be found in the ApplicationConfig class. Note that even though the application uses Java config, it is still 
inherently simpler to use the Spring Data GemFire XML namespace to configure GemFire components. The XML location is also loaded by the configuration 
class. The Spring Data Repository framework creates proxies for each repository to implement the repository methods.

NOTE: This example uses a very simple GemFire configuration appropriate for unit testing. Data is held in a local region, 
backed by a local cache and is not persisted.

In practice, GemFire should be configured so that the Repository region is distributed and replicated across multiple nodes. Disk 
persistence is also available if needed. Refer to the GemFire [documentation] (http://www.vmware.com/support/pubs/vfabric-gemfire.html)  
for more information. Also see the [spring data] (http://www.springsource.org/spring-data) project page for further details about Spring Data.  
