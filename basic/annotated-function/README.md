Spring Data Annotated Function Execution Example
==============================================

This example demonstrates the use of Annotation Driven Function Execution configured with Spring Data GemFire. The example is functionally equivalent to the [function](../basic/function) example and requires Spring Data GemFire 1.3.0 or above. The example includes a Server process which registers a function to calculate total $ales for a given product from cached orders. This requires searching all order line items for those containing the given product and computing the grand total. It is much more efficient to do this work at the server than to retrieve all orders over the network.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-annotated-function -Pmain=Server

Open a second command window, and type:

        ./gradlew -q run-annotated-function -Pmain=Client

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run the above.

# Details
The Spring configuration files, *client/cache-config.xml* and *server/cache-config.xml* contain the core gemfire cache configurations respectively. These are imported by the corresponding Spring Configuration classes, *ClientConfig* and *ServerConfig*.  

The server hosts a Product Region and Order Region and registers the function with the id with a fun id *totalSalesForProduct* as implemented by the *totalSalesForProduct* method in the *SalesFunction.java* which is a normal Spring bean.  The function is registered and wrapped in a GemFire Function by  processing the *@GemfireFunction* annotation on the method. This processing is enabled via the *@EnableGemfireFunctions* annotation in *ServerConfig.java*. 

The client invokes the function remotely using the *SalesCalculator* interface. This interface is annotated with *OnServer* which implies that all supported methods will be invoked as a Server Execution.  Note that it is not necessary for the function implementation to implement this interface as long as the implementation method defines a similar argument list.  The client side behavior is enabled via the *@EnableGemfireFunctionExecutions* annotation in *ClientConfig.java*.  This sets up a classpath scanner to discover annotated interfaces. 

Note that the server generates random orders so the totals will be different every time you start the Server.