Spring Data GemFire - Advanced Examples
========================================

These examples demonstrate additional GemFire features and require a full installation of GemFire. A trial version may be obtained [here](http://www.vmware.com/products/application-platform/vfabric-gemfire/overview.html).


#Starting and Stopping Locators

These examples require GemFire locators to be running. The locator is used to discover distributed system members and used to obtain member network addresses. For example, in a client-server topology locator addresses are known to the client and servers. The client pool uses the locator to obtain server connections on start up. Typically there are two locators for each distributed system, a primary and backup locator.

The spring-gemfire-examples-common project includes a Locator utility to start and stop locators on localhost. It is required that you defing an environment variable GEMFIRE_HOME, set to the location of the GemFire product installation. Alternatively you can pass this location as a parameter to the locator utility.

## Starting a Locator

Open a command window or terminal session, navigate to the examples directory and type
        
        gradle -q start-locator-<port-number>
        
For example, to start two locators with GEMFIRE_HOME set:

        gradle -q start-locator-10334
        gradle -q start-locator-10335

## Stopping a Locator

        gradle -q stop-locator-10334
        gradle -q stop-locator-10335
        
By default, each locator instance keeps runtime files, e.g., logs in a directory location ./locator<port-number>. Review the contents of these logs, especially if you are unable to start or stop a locator. You may also pass a command line parameter to specify an alternate working directory for a locator but you must create the directory first:

        gradle -q start-locator-10334 -Pdir=/foo/bar

## Running the Locator utility from the IDE

Import the spring-gemfire-examples-common project into your IDE. From there you should be able to run the Locator utility as a Java program, providing appropriate command line options, e.g., 

        --start --port 10334 --gemfire_home /foo/bar

If you run it with no options you will get a help message.
