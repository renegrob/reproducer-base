package com.github.renegrob;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.infinispan.Cache;
import org.infinispan.commons.api.CacheContainerAdmin;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import io.quarkus.runtime.Startup;

@Singleton
public class InfinispanCacheBean {

    private DefaultCacheManager cacheManager;
    private Cache<Object, Object> myCache;

    @PostConstruct
    public void startup() throws IOException {

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering()
                .cacheMode(CacheMode.DIST_ASYNC)
                .l1().lifespan(25000L)
                .hash().numOwners(2)
                .statistics().enable();
        Configuration cfg = builder.build();

        // TODO: Configuring Cluster Transport with Asymmetric Encryption
        cacheManager = new DefaultCacheManager(new GlobalConfigurationBuilder().transport().defaultTransport().clusterName("myCluster").build());
        //builder.clustering().cacheMode(CacheMode.LOCAL); // .memory().storage(StorageType.HEAP).maxSize("500MB").whenFull(EvictionStrategy.REMOVE); // .memory().storage(StorageType.HEAP).maxSize("500M");
        myCache = cacheManager.administration().withFlags(CacheContainerAdmin.AdminFlag.VOLATILE).getOrCreateCache("myCache", cfg);

    }

    public DefaultCacheManager getCacheManager() {
        return cacheManager;
    }

    // TODO: producer
    public Cache<Object, Object> getMyCache() {
        return myCache;
    }
}
