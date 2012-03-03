package com.gemstone.gemfire.tutorial;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main method that connects to the GemFire distributed system as a peer and
 * launches a command line user interface for the social networking application.
 * 
 * @author GemStone Systems, Inc.
 */
public class CacheServer {

	private static final String[] CONFIGS = new String[] { "app-context.xml" };

	public static void main(String[] args) throws IOException {
		String[] res = (args != null && args.length > 0 ? args : CONFIGS);
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(res);
		// shutdown the context along with the VM
		ctx.registerShutdownHook();
//	    ctx.setValidating(true);
	    ctx.refresh();
	    userMenu(ctx);
	}

	private static void userMenu(AbstractApplicationContext ctx) {
            printMenu();
            Scanner s = new Scanner(System.in);
            int choice = s.nextInt();
	}

    public static void printMenu() {
	        System.out.println();
	        System.out.println("Hit any key to exit");

	    }		

}
