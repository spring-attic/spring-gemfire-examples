package org.springframework.data.gemfire.example;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.EvictionActionType;
import org.springframework.data.gemfire.EvictionAttributesFactoryBean;
import org.springframework.data.gemfire.EvictionPolicyType;
import org.springframework.data.gemfire.ExpirationActionType;
import org.springframework.data.gemfire.ExpirationAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.EvictionAttributes;
import com.gemstone.gemfire.cache.ExpirationAttributes;
import com.gemstone.gemfire.cache.PartitionAttributes;
import com.gemstone.gemfire.cache.RegionAttributes;

/**
 * The SpringJavaBasedContainerGemFireConfiguration class is Spring Java-based Container Configuration class used to
 * configure and initialize GemFire components.
 *
 * @author John Blum
 * @see org.springframework.data.gemfire.CacheFactoryBean
 * @see org.springframework.data.gemfire.EvictionAttributesFactoryBean
 * @see org.springframework.data.gemfire.ExpirationAttributesFactoryBean
 * @see org.springframework.data.gemfire.PartitionAttributesFactoryBean
 * @see org.springframework.data.gemfire.PartitionedRegionFactoryBean
 * @see org.springframework.data.gemfire.RegionAttributesFactoryBean
 * @see com.gemstone.gemfire.cache.Cache
 * @see com.gemstone.gemfire.cache.EvictionAttributes
 * @see com.gemstone.gemfire.cache.PartitionAttributes
 * @see com.gemstone.gemfire.cache.Region
 * @see com.gemstone.gemfire.cache.RegionAttributes
 * @since 1.6.0
 */
@Configuration
@SuppressWarnings("unused")
public class SpringJavaBasedContainerGemFireConfiguration {

	// NOTE ideally, "placeholder" properties used by Spring's PropertyPlaceholderConfigurer would be externalized
	// in order to avoid re-compilation on property value changes (so... this is just an example)!
	@Bean
	public Properties placeholderProperties() {
		Properties placeholders = new Properties();

		placeholders.setProperty("app.gemfire.region.eviction.action", "LOCAL_DESTROY");
		placeholders.setProperty("app.gemfire.region.eviction.policy-type", "MEMORY_SIZE");
		placeholders.setProperty("app.gemfire.region.eviction.threshold", "4096");
		placeholders.setProperty("app.gemfire.region.expiration.entry.tti.action", "INVALIDATE");
		placeholders.setProperty("app.gemfire.region.expiration.entry.tti.timeout", "300");
		placeholders.setProperty("app.gemfire.region.expiration.entry.ttl.action", "DESTROY");
		placeholders.setProperty("app.gemfire.region.expiration.entry.ttl.timeout", "600");
		placeholders.setProperty("app.gemfire.region.partition.local-max-memory", "16384");
		placeholders.setProperty("app.gemfire.region.partition.redundant-copies", "1");
		placeholders.setProperty("app.gemfire.region.partition.total-max-memory", "32768");

		return placeholders;
	}

	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(
			@Qualifier("placeholderProperties") Properties placeholders) {
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setProperties(placeholders);
		return propertyPlaceholderConfigurer;
	}

	@Bean
	public Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", "SpringGemFireJavaConfigTest");
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", "config");

		return gemfireProperties;
	}

	@Bean
	@Autowired
	public CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) throws Exception {
		CacheFactoryBean cacheFactory = new CacheFactoryBean();
		cacheFactory.setProperties(gemfireProperties);
		return cacheFactory;
	}

	/*
	  NOTE need to qualify the RegionAttributes bean definition reference since GemFire's
	  com.gemstone.gemfire.internal.cache.AbstractRegion class "implements" RegionAttributes (face-palm),
	  which led Spring to the following Exception...

	  java.lang.IllegalStateException: Failed to load ApplicationContext ...
	  Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException:
	  Error creating bean with name 'ExamplePartition' defined in class org.spring.data.gemfire.config.GemFireConfiguration:
	  Unsatisfied dependency expressed through constructor argument with index 1 of type [com.gemstone.gemfire.cache.RegionAttributes]:
	  No qualifying bean of type [com.gemstone.gemfire.cache.RegionAttributes] is defined:
	  expected single matching bean but found 2: ExampleLocal,defaultRegionAttributes;
	  nested exception is org.springframework.beans.factory.NoUniqueBeanDefinitionException:
	  No qualifying bean of type [com.gemstone.gemfire.cache.RegionAttributes] is defined:
	  expected single matching bean but found 2: ExampleLocal,defaultRegionAttributes
	  */
	@Bean(name = "ExamplePartition")
	@Autowired
	public PartitionedRegionFactoryBean<Object, Object> examplePartitionRegion(Cache gemfireCache,
			@Qualifier("partitionRegionAttributes") RegionAttributes<Object, Object> regionAttributes) throws Exception {

		PartitionedRegionFactoryBean<Object, Object> examplePartitionRegion =
			new PartitionedRegionFactoryBean<Object, Object>();

		examplePartitionRegion.setAttributes(regionAttributes);
		examplePartitionRegion.setCache(gemfireCache);
		examplePartitionRegion.setName("ExamplePartitionRegion");
		examplePartitionRegion.setPersistent(false);

		return examplePartitionRegion;
	}

	@Bean
	@Autowired
	public RegionAttributesFactoryBean partitionRegionAttributes(PartitionAttributes partitionAttributes,
			EvictionAttributes evictionAttributes,
			@Qualifier("entryTtiExpirationAttributes") ExpirationAttributes entryTti,
			@Qualifier("entryTtlExpirationAttributes") ExpirationAttributes entryTtl) {

		RegionAttributesFactoryBean regionAttributes = new RegionAttributesFactoryBean();

		regionAttributes.setEvictionAttributes(evictionAttributes);
		regionAttributes.setEntryIdleTimeout(entryTti);
		regionAttributes.setEntryTimeToLive(entryTtl);
		regionAttributes.setPartitionAttributes(partitionAttributes);

		return regionAttributes;
	}

	@Bean
	public EvictionAttributesFactoryBean defaultEvictionAttributes(
			@Value("${app.gemfire.region.eviction.action}") String action,
			@Value("${app.gemfire.region.eviction.policy-type}") String policyType,
			@Value("${app.gemfire.region.eviction.threshold}") int threshold) {

		EvictionAttributesFactoryBean evictionAttributes = new EvictionAttributesFactoryBean();

		evictionAttributes.setAction(EvictionActionType.valueOfIgnoreCase(action).getEvictionAction());
		evictionAttributes.setThreshold(threshold);
		evictionAttributes.setType(EvictionPolicyType.valueOfIgnoreCase(policyType));

		return evictionAttributes;
	}

	@Bean
	public ExpirationAttributesFactoryBean entryTtiExpirationAttributes(
			@Value("${app.gemfire.region.expiration.entry.tti.action}") String action,
			@Value("${app.gemfire.region.expiration.entry.tti.timeout}") int timeout) {

		ExpirationAttributesFactoryBean expirationAttributes = new ExpirationAttributesFactoryBean();

		expirationAttributes.setAction(ExpirationActionType.valueOfIgnoreCase(action).getExpirationAction());
		expirationAttributes.setTimeout(timeout);

		return expirationAttributes;
	}

	@Bean
	public ExpirationAttributesFactoryBean entryTtlExpirationAttributes(
			@Value("${app.gemfire.region.expiration.entry.ttl.action}") String action,
			@Value("${app.gemfire.region.expiration.entry.ttl.timeout}") int timeout) {

		ExpirationAttributesFactoryBean expirationAttributes = new ExpirationAttributesFactoryBean();

		expirationAttributes.setAction(ExpirationActionType.valueOfIgnoreCase(action).getExpirationAction());
		expirationAttributes.setTimeout(timeout);

		return expirationAttributes;
	}

	@Bean
	public PartitionAttributesFactoryBean defaultPartitionAttributes(
			@Value("${app.gemfire.region.partition.local-max-memory}") int localMaxMemory,
			@Value("${app.gemfire.region.partition.redundant-copies}") int redundantCopies,
			@Value("${app.gemfire.region.partition.total-max-memory}") int totalMaxMemory) {

		PartitionAttributesFactoryBean partitionAttributes = new PartitionAttributesFactoryBean();

		partitionAttributes.setLocalMaxMemory(localMaxMemory);
		partitionAttributes.setRedundantCopies(redundantCopies);
		partitionAttributes.setTotalMaxMemory(totalMaxMemory);

		return partitionAttributes;
	}

}
