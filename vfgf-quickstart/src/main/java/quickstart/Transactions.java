package quickstart;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gemstone.gemfire.cache.Region;

/**
 * You can execute transactions against the data in the distributed cache as you
 * would in a database. GemFire transactions are compatible with Java
 * Transaction API (JTA) transactions. Please refer to the quickstart guide for
 * instructions on how to run this example.
 * 
 * @author GemStone Systems, Inc.
 * @since 4.1.1
 */
@Component
public class Transactions {

	@Resource(name = "exampleRegionTemplate")
	private GemfireTemplate gemfireTemplate;

	@Transactional(rollbackFor = RuntimeException.class)
	public void populateAndRollback() {
		for (int count = 0; count < 3; count++) {
			String key = "key" + count;
			String value = "RollbackValue" + count;
			System.out.println("Putting entry: " + key + ", " + value);
			gemfireTemplate.put(key, value);
		}
		throw new RuntimeException(
				"Exception thrown to cause rollback or transaction");
	}

	@Transactional
	public void populateRegionAndCommit() {
		for (int count = 0; count < 3; count++) {
			String key = "key" + count;
			String value = "CommitValue" + count;
			System.out.println("Putting entry: " + key + ", " + value);
			gemfireTemplate.put(key, value);
		}
	}

	public void printRegionData() {
		@SuppressWarnings("unchecked")
		Set<String> keySet = new TreeSet<String>((Set<String>) gemfireTemplate
				.getRegion().keySet());
		for (Object entryKey : keySet) {
			Region.Entry<?, ?> entry = gemfireTemplate.getRegion().getEntry(
					entryKey);
			Object entryValue = entry.getValue();
			System.out
					.println("        entry: " + entryKey + ", " + entryValue);
		}
	}
}
