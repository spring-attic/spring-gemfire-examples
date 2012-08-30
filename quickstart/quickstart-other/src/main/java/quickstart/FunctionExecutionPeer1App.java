package quickstart;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import quickstart.function.execution.FunctionExecutionPeer1;

public class FunctionExecutionPeer1App {

	private static final String[] CONFIGS = new String[] { "function-execution-peer-app-context.xml" };

	public static void main(String[] args) {

		try {

			String[] res = (args != null && args.length > 0 ? args : CONFIGS);
			AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
					res);
			// shutdown the context along with the VM
			ctx.registerShutdownHook();
			FunctionExecutionPeer1 bean = ctx
					.getBean(FunctionExecutionPeer1.class);
			bean.run();
				 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
