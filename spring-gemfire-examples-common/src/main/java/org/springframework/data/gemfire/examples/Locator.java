package org.springframework.data.gemfire.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.gemstone.gemfire.admin.AdminDistributedSystem;
import com.gemstone.gemfire.admin.AdminDistributedSystemFactory;
import com.gemstone.gemfire.admin.AdminException;
import com.gemstone.gemfire.admin.DistributedSystemConfig;
import com.gemstone.gemfire.admin.DistributionLocator;
import com.gemstone.gemfire.admin.DistributionLocatorConfig;

public class Locator {
	static final int DEFAULT_PORT = 10334;
	static final int MAX_WAIT_TIME = 15000;
	
	public static void main(String args[])  {

		if (!(System.getenv().containsKey("GEMFIRE_HOME")) ||
				System.getenv().get("GEMFIRE_HOME") == null ||
				System.getenv().get("GEMFIRE_HOME").isEmpty()){
	    	System.out.println("The environment variable GEMFIRE_HOME in not defined");
	    	System.exit(1);
	    }  
	
        Map<String, Object> options = LocatorCommandParser.parseOptions(args);
        if (options == null) {
            System.exit(1);
        }

        int port = DEFAULT_PORT;
        if (options.containsKey("port")) {
            port = (Integer) options.get("port");
        }   

        DistributedSystemConfig distributedSystemConfig = AdminDistributedSystemFactory.defineDistributedSystem();

        AdminDistributedSystem adminDistributedSystem = AdminDistributedSystemFactory
                .getDistributedSystem(distributedSystemConfig);
       
       
        DistributionLocatorConfig locatorConfig = adminDistributedSystem.addDistributionLocator().getConfig();
        locatorConfig.setHost("localhost");
        locatorConfig.setPort(port);
        
        String workingDir = (String)options.get("dir");
        
        /*
         * If using the default, create the directory 
         */
        File locatorDir = null;
        if (workingDir == null) {
        	workingDir = new File(".").getAbsolutePath() + File.separator + "locator" + port;
        	 locatorDir = new File(workingDir);
             if (!locatorDir.exists()) {
             	locatorDir.mkdir();
             }
        } 
        /*
         * If directory passed as a command argument, it must exist
         */
        else {
        	locatorDir = new File (workingDir);
        	if (!locatorDir.exists()) {
        		System.err.println(" Directory " + workingDir + " does not exist.");
        		System.exit(1);
        	}
        }
        
        System.out.println("\nSetting working directory to " + workingDir);
        
       
        
        locatorConfig.setWorkingDirectory(workingDir);
        
        String propertiesFile = (String)options.get("properties");
   
        if (propertiesFile != null && options.get("command").equals("start")){
        	FileInputStream is;
        	Properties properties = new Properties();
        	properties.put("mcast-port", "0");
        	
			try {
				is = new FileInputStream(propertiesFile);
				properties.load(is);
			}
			catch (FileNotFoundException e) {
				 System.out.println("cannot find properties file :" + propertiesFile);
				 System.exit(1);
			}
			catch (IOException e) {
				System.out.println("cannot read properties file :" + propertiesFile);
				System.exit(1);
			}
        	
        	locatorConfig.setDistributedSystemProperties(properties);
        }
       
        locatorConfig.setProductDirectory(System.getenv().get("GEMFIRE_HOME"));

        for (DistributionLocator locator : adminDistributedSystem.getDistributionLocators()) {

            try {
                if (options.get("command").equals("stop")) {
                    if (stopLocator(locator, MAX_WAIT_TIME)) {
                        System.out.println(String.format("locator stopped on %s[%s]", locator.getConfig().getHost(),
                                locator.getConfig().getPort()));
                    } else {
                        System.out.println(String.format("failed to stop locator on %s[%s]", locator.getConfig()
                                .getHost(), locator.getConfig().getPort()));
                    }
                }

                if (options.get("command").equals("start")) {
                	
                	for (File file : locatorDir.listFiles( new FilenameFilter(){
						@Override
						public boolean accept(File dir, String name) {
							return name.endsWith(".log") || name.endsWith(".dat");
						}
                		
                	}) ){
                		file.delete();
                	}
                	
                    if (startLocator(locator, MAX_WAIT_TIME)) {
                        System.out.println(String.format("locator running on %s[%s]", locator.getConfig().getHost(),
                                locator.getConfig().getPort()));
                      for (Entry<Object,Object> prop: locator. getConfig().getDistributedSystemProperties().entrySet()) {
                    	  System.out.println( prop.getKey() + "=" + prop.getValue());
                      }
                    } else {
                        System.out.println(String.format("failed to start locator on %s[%s]", locator.getConfig()
                                .getHost(), locator.getConfig().getPort()));
                    }
                }
              
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    static boolean stopLocator(DistributionLocator locator, int maxWaitTime) throws AdminException,
            InterruptedException {
        if (locator.isRunning()) {
            locator.stop();
            int waitTime = 0;
            while (waitTime < maxWaitTime && locator.isRunning()) {
                locator.waitToStop(1000);
                waitTime += 1000;
            }
        }
        return !locator.isRunning();
    }

    static boolean startLocator(DistributionLocator locator, int maxWaitTime) throws AdminException,
            InterruptedException {
        if (!locator.isRunning()) {
            locator.start();
            int waitTime = 0;
            while (waitTime < maxWaitTime && !locator.isRunning()) {
                locator.waitToStart(1000);
                waitTime += 1000;
            }
        }
        return locator.isRunning();
    }
}
