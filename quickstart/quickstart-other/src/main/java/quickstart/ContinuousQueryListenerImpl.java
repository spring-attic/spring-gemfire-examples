package quickstart;

import com.gemstone.gemfire.cache.Operation;
import com.gemstone.gemfire.cache.query.CqEvent;

public class ContinuousQueryListenerImpl {

	public void handleEvent(CqEvent event) {
		Operation baseOperation = event.getBaseOperation();
		Operation queryOperation = event.getQueryOperation();

		String baseOp = "";
		String queryOp = "";

		if (baseOperation.isUpdate()) {
			baseOp = " Update";
		}
		else if (baseOperation.isCreate()) {
			baseOp = " Create";
		}
		else if (baseOperation.isDestroy()) {
			baseOp = " Destroy";
		}
		else if (baseOperation.isInvalidate()) {
			baseOp = " Invalidate";
		}

		if (queryOperation.isUpdate()) {
			queryOp = " Update";
		}
		else if (queryOperation.isCreate()) {
			queryOp = " Create";
		}
		else if (queryOperation.isDestroy()) {
			queryOp = " Destroy";
		}

		StringBuffer eventLog = new StringBuffer();
		eventLog.append("\n    "
				+ "CqListener:\n    Received cq event for entry: "
				+ event.getKey() + ", "
				+ ((event.getNewValue()) != null ? event.getNewValue() : "")
				+ "\n" + "    With BaseOperation =" + baseOp
				+ " and QueryOperation =" + queryOp + "\n");
		System.out.print(eventLog.toString());

	}

}
