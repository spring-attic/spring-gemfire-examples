package org.springframework.data.gemfire.examples;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class LocatorCommandParser {
	private static Options options = new Options();
	private static void initOptions() {
		options.addOption("start", false, "Start the locator");
		options.addOption("stop", false, "Stop the locator");
		options.addOption("status", false, "Check locator status");
		options.addOption("port", true, "the locator port");
		options.addOption("dir", true, "the locator working directory");
		options.addOption("properties", true, "a gemfire properties file");
		options.addOption("help", false, "display this message");
	}
	 
	public static Map<String,Object> parseOptions(String [] args) {
		initOptions();
		HashMap<String,Object> optionsMap = new HashMap<String,Object>();
		BasicParser parser = new BasicParser();
		
		try {
			CommandLine cl = parser.parse(options, args);
			if (cl.hasOption("help")){
			   usage("help");
			   return null;
			}
			
			String command = getCommand(cl);
			
			if (command==null){
				return null;
			}
		
		    optionsMap.put("command",command);	
		    
		    if (cl.hasOption("port")){
				optionsMap.put("port",Integer.parseInt(cl.getOptionValue("port")));	
			}
		    
		    if (cl.hasOption("dir")){
				optionsMap.put("dir",cl.getOptionValue("dir"));	
			}
		    
		    if (cl.hasOption("properties")){
				optionsMap.put("properties",cl.getOptionValue("properties"));	
		    }   
		}
		
		catch (ParseException e) {
			usage(e.getMessage());
			return null;
		}
		
		return optionsMap;
		
	}
	
	private static String getCommand(CommandLine cl) {
		String command = null;
		if(cl.hasOption("start")){
			command = "start";
	    } 
		
		if(cl.hasOption("stop")){
			if (command == null){
			  command = "stop";
			} else {
				  usage("ERROR: Invalid command option: 'stop'. Command already provided '" + command + "'." );
				  return null;
			}
	    } 
		
		if (command==null){
			 usage("ERROR: No command option provided. One of 'start', 'stop' or 'status' is required.");
		}
			
		return command; 
	}

	private static void usage(String msg){
		 HelpFormatter f = new HelpFormatter();
		 f.printHelp(msg, options);
	}

}
