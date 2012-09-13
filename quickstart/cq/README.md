Spring Continuous Query Example
===============================

This example demonstrates how to process Continuous Queries with Spring CQListenerContainer. 

This example requires a client-server configuration. The CacheServer starts a cache server and updates the region. The CacheClient 
configures a CQ Listener which handles CQ Events received. 


To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-cq -Pmain=Server

in another window:

        ./gradlew -q run-qc -Pmain=Client

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run the above classes


# Details

Note that CQListener is a POJO (albeit dependent on the GemFire type CQEvent). It is not required to implement any specific interface, but it must provide appropriate method signatures so the CQListenerContainer can delegate CQEvents to it. The CacheClient's pool must be configured with subscription-enabled='true'.

When the Cache Server starts, you will be prompted to update the cache. This will create three entries in the 'Order' region. You must start the Cache Client before doing this since the client is not configured as a durable subscription.   Upon updating the cache, the client should receive two order events matching the CQ criteria. 
