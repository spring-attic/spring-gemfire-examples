Partitioned Region Example
==========================

This demonstrates using Spring Data GemFire to create a partitioned region. The demonstration contains a Client and a Server. The partitioned region will cause entries to be distributed among the active servers. This demonstration is designed for two server instances.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        gradle -q run-partitioned -PmainClass=Server

Open a second command window and repeat the above command

When both servers are running, open a third window, and type:
        gradle -q run-partitioned -PmainClass=Client

Or to run from your IDE, execute one of the following tasks once.

        gradle eclipse
        gradle idea 

Then import the project into your IDE and run the above classes

# Details

When the Client runs, it will create 100 Orders. These will be evenly distributed across the two servers as indicated by the log messages. Notice the key values for each entry show a more or less random pattern. 

# Custom Partitioning

The example also includes a custom PartitionResolver (CountryPartitionResolver) that allows the application to control how entries are co-located. In this example, the order shipping address is either in the US or the UK. In fact, all the odd number orders go to UK, and the evens go to US. The Spring configuration file server/cache-config is configured with 3 Spring profiles, "default", "UK", and "US". In the "US" profile, the region is configured with a fixed partition named "US" and likewise the "UK" profile contains the "UK" partition. Both profiles are configured with the CountryPartitionResolver which routes orders to corresponding partition. So try this:

In the server command windows, run the servers again, but each with a different active profile:

          gradle -q run-partitioned -PmainClass=Server -PjavaArgs=US
          gradle -q run-partitioned -PmainClass=Server -PjavaArgs=UK

Run the client again. This time, you will observe that all the odd numbered orders go to "UK" and the evens go to "US"
