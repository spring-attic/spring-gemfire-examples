Spring Data GemFire - Gateway V7 Example
=====================================

See the [README](..README.md) in the advanced root directory for information about locators. 

Note: This example requires a valid [GemFire license key](http://pubs.vmware.com/vfabric51/topic/com.vmware.vfabric.gemfire.6.6/deploying/licensing/licensing.html?resultof=%22%6c%69%63%65%6e%73%69%6e%67%22%20%22%6c%69%63%65%6e%73%22%20) for a Data Management Node. GemFire checks for a valid license before starting a gateway.

If you have downloaded the trial version, you will need to obtain a key from GemFire sales. Edit gateway.properties to set the license key:

        gemfire.license.key=my license key

To run this example, Open a command window and start two locators:

        ./gradlew -q start-locator-10334 -Pprops=advanced/gatewayV7/london/locator.properties
        ./gradlew -q start-locator-10335 -Pprops=advanced/gatewayV7/ny/locator.properties
        
Each properties file declares the other locator as a remote locator and identifies the remote system Id.        


Start one of the processes:

        ./gradlew -q run-gatewayV7 -Pmain=NewYork

In another command window, run:

        ./gradlew -q run-gatewayV7 -Pmain=London

Each of these initializes an Order region which is gateway enabled and a gateway hub. Since each process uses a different locator, and multicast is disabled (mcast-port=0), each cache runs in a separate Distributed System, simulating two remote installations. Both processes will prompt you to press ENTER to update the region. Wait for both processes to start first. You should see a log message from the remote process's LoggingCacheListener indicating the remote region was updated. Press ENTER a second time to terminate the process.
