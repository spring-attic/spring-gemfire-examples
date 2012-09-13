Spring Data GemFire - Locator Failover Example
==============================================

See the [README](..README.md) in the advanced root directory for information about locators. 

This example creates simple a client server system to demonstrate what happens when the primary locator is down either after initialization or before initialization. 

# Scenario 1 - Locator Down After Initialization

Start two locators:

        ./gradlew -q start-locator-10334
        ./gradlew -q start-locator-10335

Start the server:

        ./gradlew -q run-locator-failover -Pmain=Server

Start the client:

        ./gradlew -q run-locator-failover -Pmain=Client


The client will create two Customer entries which will be indicated by Server log messages.

When prompted in the client, stop the primary locator:

        ./gradlew -q stop-locator-10334

The client will create two more Customer entries.

You should see some warning messages in the Server process like:

        Could not connect to distribution locator  localhost<v0>:10334: java.net.ConnectException: Connection refused

But everything should still work. In this case, the client already has a connection to the server so the locator is not used. 


#Scenario 2 - Locator Down Before Initialization

Following scenario 1, the primary locator (port 10334) is stopped and the secondary locator (port 10335) is running.  You may also leave the Server running or restart it. When the Client starts, you will see an info log message something like:

        [info ... EDT <poolTimer-gemfirePool-2> tid=0x17] locator localhost/127.0.0.1:10334 is not running.
                java.net.ConnectException: Connection refused ... (stack trace follows)

The secondary locator will take over and the application will run as before.



