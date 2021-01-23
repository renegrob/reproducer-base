package com.github.renegrob;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

import io.quarkus.runtime.Startup;

@Singleton
public class InfinispanCacheBean {

    private DefaultCacheManager cacheManager;
    private Cache<Object, Object> myCache;

    @PostConstruct
    public void startup() {
        // https://infinispan.org/docs/10.0.x/titles/embedding/embedding.html#using_infinispan_as_an_embedded_data_grid_in_java_se
        /*
        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder().transport()
                .defaultTransport()
                .clusterName("dev-cluster")
                .addProperty("configurationFile", "default-jgroups-tcp.xml")
                .build();

        // TODO: Configuring Cluster Transport with Asymmetric Encryption
        cacheManager = new DefaultCacheManager(globalConfig);

         */
        cacheManager = new DefaultCacheManager();
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering().cacheMode(CacheMode.REPL_SYNC).memory().storage(StorageType.HEAP).maxSize("500M");
        myCache = cacheManager.administration().getOrCreateCache("myCache", builder.build());
    }

    /*
    @Produces
    public Configuration defaultEmbeddedConfiguration () {
        return new ConfigurationBuilder()
                         .eviction()
                                 .strategy(EvictionStrategy.LRU)
                                 .maxEntries(100)
                         .build();
    }

    	@Produces
	@ApplicationScoped
	public EmbeddedCacheManager defaultClusteredCacheManager() {
		GlobalConfiguration g = new GlobalConfigurationBuilder()
				.clusteredDefault()
				.transport()
				.clusterName("InfinispanCluster")
				.build();
		Configuration cfg = new ConfigurationBuilder()
				.eviction()
				.strategy(EvictionStrategy.LRU)
				.maxEntries(150)
				.build();
		return new DefaultCacheManager(g, cfg);
	}
     */

    public DefaultCacheManager getCacheManager() {
        return cacheManager;
    }

    // TODO: producer
    public Cache<Object, Object> getMyCache() {
        return myCache;
    }
}
