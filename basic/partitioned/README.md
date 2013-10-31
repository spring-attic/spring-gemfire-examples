Partitioned Region Example
==========================

This demonstrates using Spring Data GemFire to create a partitioned region. The demonstration contains a Client and a Server. The partitioned region will cause entries to be distributed among the active servers. This demonstration is designed for two server instances.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-partitioned -Pmain=Server

Open a second command window and repeat the above command

When both servers are running, open a third window, and type:
        ./gradlew -q run-partitioned -Pmain=Client

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run the above classes

# Details

When the Client runs, it will create 100 Orders. These will be evenly distributed across the two servers as indicated by the log messages. Notice the key values for each entry show a more or less random pattern. 

# Custom Partitioning

The example also demonstrates the use of a custom PartitionResolver that allows the application to control how entries are co-located. In this example, the order shipping address is either in the US or the UK. To enable partitioning by country, an alternate implementation of the key class (OrderKey) that implements PartitionResolver replaces the normal key class. 

Run the the client again, adding an argument:

          ./gradlew -q run-partitioned -Pmain=Client -Pargs=partitionByCountry
        
This time, observe that the server console log indicates orders are partitioned by country.
