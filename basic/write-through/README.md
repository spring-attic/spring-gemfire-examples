Write Through Example
==========================

This demonstrates using Spring Data GemFire to create a CacheLoader and CacheWriter to synchronize data with a relational database. Database synchronization
may be done synchronously (write-through) or asynchronously (write-behind). 

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-write-through

To demonstrate write-behind, pass an 'async' argument to the command line:

         ./gradlew -q run-write-through -Pargs=async

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run Main.java

# Details
The cache-config.xml configures A Product region with a CacheLoader (ProductDBLoader.java) and CacheWriter (ProductDBWriter.java). An embedded H2 database is initialized with a corresponding Product table with 3 rows. The example accesses this data using via the cache and the entries are lazily loaded from the database. Any updates are written to the database. 

A Spring Data JPA Repository is used to access the database. The 'async' command line argument enables the 'async' Spring profile which enables the asynchronous behavior in ProductDBWriter. You can verify this by the thread names shown in the log messages (e.g. 'main' and 'SimpleAsyncTaskExecutor-#').
