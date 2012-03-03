package com.gemstone.gemfire.tutorial;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.tutorial.storage.GemfireClientDAO;
import com.gemstone.gemfire.tutorial.storage.IGemfireDAO;

/**
 * Main method that connects to the GemFire distributed system as a client and
 * launches a command line user interface for the social networking application.
 * 
 * @author GemStone Systems, Inc.
 */
@Component
public class Client {
	private static final String[] CONFIGS = new String[] { "spring-cache-client-context.xml" };

	public static void main(String[] args) throws IOException {
		String[] res = (args != null && args.length > 0 ? args : CONFIGS);
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(res);
		// shutdown the context along with the VM
		ctx.registerShutdownHook();

		// call greet world to prevent the thread from ending
		IGemfireDAO dao = ctx.getBean(GemfireClientDAO.class);
		GemfireClientDAO clientDAO = (GemfireClientDAO) dao;
		clientDAO.initClient();
		TextUI ui = new TextUI(clientDAO, System.in, System.out);
		ui.processCommands();
	}

}
