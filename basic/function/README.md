Spring Data GemFire Function Execution Example
==============================================

This example demonstrates the use of GemFire's Function Execution Service configured with Spring Data GemFire. The example contains a Server which registers a function to calculate total $ales for a given product from orders in cache. This requires searching all order line items for the given product and computing the grand total. It is much more efficient to do this work at the server than to retrieve all orders over the network.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-function -Pmain=Server

Open a second command window, and type:

        ./gradlew -q run-function -Pmain=Client

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run the above.

# Details
The Spring configuration files, client/cache-config.xml and server/cache-config.xml contain the respective configurations. The client is configured with a client pool. The server hosts a Product Region and Order Region and uses the FunctionService to register a function CalculateTotalSalesForProduct. This is a Spring bean that implements GemFire's Function interface. The client invokes the function remotely using CalculateTotalSalesForProductInvoker. Note that the server generates random orders so the totals will be different every time you start the Server.

Also Note that since the client and the server side components are all in the same classpath, Spring's compont scanner includes filters to ensure client side beans are not registered in the server application context, and vice versa. In a real application, it would be better to package server side functions in a separate jar and deploy that to the server. 
