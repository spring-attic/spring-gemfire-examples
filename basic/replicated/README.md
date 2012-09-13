Replicated Region Example
==========================

This demonstrates using Spring Data GemFire to create a replicated region. The demonstration contains a Producer and a Consumer that share a replicated region.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-replicate -Pmain=Consumer

When instructed, open a second command window, and type:

        ./gradlew -q run-replicate -Pmain=Producer

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run the above classes

# Details
The Spring configuration files, consumer/cache-config.xml and producer/cache-config.xml are nearly identical. The main difference is that the Producer version has data-policy="empty" in the replicated-region configuration. This means that The Producer does not cache data but uses the region to write data which wil be shared with peers (Consumer in this case). The Consumer caches the data in its replica and may also create or update data but these changes won't be seen by the Producer.
