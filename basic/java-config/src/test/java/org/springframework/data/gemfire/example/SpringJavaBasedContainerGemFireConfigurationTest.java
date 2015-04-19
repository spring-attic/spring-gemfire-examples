package org.springframework.data.gemfire.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.EvictionAction;
import com.gemstone.gemfire.cache.EvictionAlgorithm;
import com.gemstone.gemfire.cache.EvictionAttributes;
import com.gemstone.gemfire.cache.ExpirationAction;
import com.gemstone.gemfire.cache.ExpirationAttributes;
import com.gemstone.gemfire.cache.PartitionAttributes;
import com.gemstone.gemfire.cache.Region;

/**
 * The SpringJavaBasedContainerGemFireConfigurationTest class is a test suite of test cases testing the configuration
 * of GemFire Cache components defined as Spring beans in a Spring context configured with Java-based Container
 * Configuration meta-data.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.junit.runner.RunWith
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringJUnit4ClassRunner
 * @see com.gemstone.gemfire.cache.Cache
 * @see com.gemstone.gemfire.cache.EvictionAttributes
 * @see com.gemstone.gemfire.cache.ExpirationAttributes
 * @see com.gemstone.gemfire.cache.PartitionAttributes
 * @see com.gemstone.gemfire.cache.Region
 * @since 1.6.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringJavaBasedContainerGemFireConfiguration.class })
@SuppressWarnings("unused")
public class SpringJavaBasedContainerGemFireConfigurationTest {

	@Autowired
	private Cache gemfireCache;

	@Resource(name = "ExamplePartition")
	private Region<Object, Object> examplePartition;

	protected void assertEvictionAttributes(EvictionAttributes evictionAttributes,
			EvictionAction expectedAction, EvictionAlgorithm expectedAlgorithm, int expectedMaximum) {
		assertNotNull("EvictionAttributes must not be null!", evictionAttributes);
		assertEquals(expectedAction, evictionAttributes.getAction());
		assertEquals(expectedAlgorithm, evictionAttributes.getAlgorithm());
		assertEquals(expectedMaximum, evictionAttributes.getMaximum());
	}

	protected void assertExpirationAttributes(ExpirationAttributes expirationAttributes,
			ExpirationAction expectedAction, int expectedTimeout) {
		assertNotNull("ExpirationAttributes must not be null!", expirationAttributes);
		assertEquals(expectedAction, expirationAttributes.getAction());
		assertEquals(expectedTimeout, expirationAttributes.getTimeout());
	}

	protected void assertPartitionAttributes(PartitionAttributes partitionAttributes,
			int expectedLocalMaxMemory, int expectedRedundantCopies, int expectedTotalMaxMemory) {
		assertNotNull("PartitionAttributes must not be null!", partitionAttributes);
		assertEquals(expectedLocalMaxMemory, partitionAttributes.getLocalMaxMemory());
		assertEquals(expectedRedundantCopies, partitionAttributes.getRedundantCopies());
		assertEquals(expectedTotalMaxMemory, partitionAttributes.getTotalMaxMemory());
	}

	protected void assertRegion(Region<?, ?> region, String expectedNamePath, DataPolicy expectedDataPolicy) {
		assertRegion(region, expectedNamePath, String.format("%1$s%2$s", Region.SEPARATOR, expectedNamePath),
			expectedDataPolicy);
	}

	protected void assertRegion(Region<?, ?> region, String expectedName, String expectedPath, DataPolicy expectedDataPolicy) {
		assertNotNull(String.format("Region '%1$s' was not properly configured and initialized!", expectedPath), region);
		assertEquals(expectedName, region.getName());
		assertEquals(expectedPath, region.getFullPath());
		assertNotNull(region.getAttributes());
		assertEquals(expectedDataPolicy, region.getAttributes().getDataPolicy());
	}

	@Test
	public void testGemFireCacheConfiguration() {
		assertNotNull("The GemFire Cache was not properly configured and initialized!", gemfireCache);
		assertNotNull(gemfireCache.getDistributedSystem());
		assertNotNull(gemfireCache.getDistributedSystem().getProperties());
		assertEquals("SpringGemFireJavaConfigTest", gemfireCache.getDistributedSystem().getProperties().getProperty("name"));
		assertEquals("0", gemfireCache.getDistributedSystem().getProperties().getProperty("mcast-port"));
		assertEquals("config", gemfireCache.getDistributedSystem().getProperties().getProperty("log-level"));
	}

	@Test
	public void testPartitionRegionConfiguration() {
		assertRegion(examplePartition, "ExamplePartitionRegion", DataPolicy.PARTITION);
		assertPartitionAttributes(examplePartition.getAttributes().getPartitionAttributes(), 16384, 1, 32768);
		assertExpirationAttributes(examplePartition.getAttributes().getEntryIdleTimeout(),
			ExpirationAction.INVALIDATE, 300);
		assertExpirationAttributes(examplePartition.getAttributes().getEntryTimeToLive(),
			ExpirationAction.DESTROY, 600);
		assertEvictionAttributes(examplePartition.getAttributes().getEvictionAttributes(),
			EvictionAction.LOCAL_DESTROY, EvictionAlgorithm.LRU_MEMORY, examplePartition.getAttributes()
				.getPartitionAttributes().getLocalMaxMemory());
	}

}
