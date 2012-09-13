Persistent Regions Example
==========================

This demonstrates using Spring Data GemFire to create persistent regions. Persistent regions back up entries to disk and are initialized with persistent data. Initially there is no data stored on disk. After running the application once, it will create some data which GemFire will persist in its disk stores. Each time you run the application you will be prompted to delete the disk stores or not. If you do not delete the disk files, the last created entries will still be available.

To run this example, open a command window, go to the the spring-gemfire-examples root directory, and type:

        ./gradlew -q run-persistence

Or to run from your IDE, execute one of the following tasks once.

        ./gradlew eclipse
        ./gradlew idea 

Then import the project into your IDE and run Main.java

# Details
The cache-config.xml configures two persistent regions, one for Products and one for Orders. Order is configured for disk overflow with a maximum number of entries held in memory. Accessing this data is transparent to the application. Evicted entries are restored from disk when accessed via the Region apis.
