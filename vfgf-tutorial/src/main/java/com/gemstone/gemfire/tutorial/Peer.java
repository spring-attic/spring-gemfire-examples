package com.gemstone.gemfire.tutorial;

import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.tutorial.storage.GemfireDAO;

/**
 * Main method that connects to the GemFire distributed system as a peer and
 * launches a command line user interface for the social networking application.
 * 
 * @author GemStone Systems, Inc.
 */
public class Peer {

	private static final String[] CONFIGS = new String[] { "app-context.xml" };

	public static void main(String[] args) throws IOException {
		String[] res = (args != null && args.length > 0 ? args : CONFIGS);
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(res);
		// shutdown the context along with the VM
		ctx.registerShutdownHook();

		// call greet world to prevent the thread from ending
		GemfireDAO dao = ctx.getBean(GemfireDAO.class);
	
//		dao.initPeer();
		TextUI ui = new TextUI(dao, System.in, System.out);
		ui.processCommands();
	}

}
