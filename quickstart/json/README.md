Spring Data Gemfire JSON Example
================================

This demonstrates Spring Framework's GemfireTemplate to access the Gemfire cache with automatic JSON conversion.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-json

Or to run from your IDE, run the following command once for all projects.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run Main.java

#Details

This simple example demonstrates the use of GemfireTemplate to perform region data access for data stored as JSON. Spring converts objects to GemFire's internal JSON representation before writing then to the cache. This feature is enabled in src/main/resources/cache-config with the following configuration:

	<gfe-data:json-region-autoproxy/>

This enables Spring AOP to add advise around appropriate read and write operations on Region and GemfireTemplate to perform the necessary conversions. *Note that target regions and/or GemfireTemplates must be declared as Spring beans for this to work.* 

