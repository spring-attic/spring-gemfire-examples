Replicated Region (Client Server) Example
==========================================

This demonstrates using Spring Data GemFire to create a replicated region. The demonstration contains a Client and a Server that hosts a replicated region.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-replicate-cs -Pmain=Server

Open a second command window, and type:

        ./gradlew -q run-replicate-cs -Pmain=Client

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run the above.

# Details
The Spring configuration files, client/cache-config.xml and server/cache-config.xml contain the respective configurations. The client is configured with a client region to access the same region hosted on the server. 
