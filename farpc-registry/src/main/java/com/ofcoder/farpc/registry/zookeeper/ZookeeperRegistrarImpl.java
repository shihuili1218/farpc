package com.ofcoder.farpc.registry.zookeeper;

import com.ofcoder.farpc.cluster.ILoadbalance;
import com.ofcoder.farpc.cluster.LoadbalanceFactory;
import com.ofcoder.farpc.cluster.loadbalance.RandomLoadbalanceImpl;
import com.ofcoder.farpc.registry.IRegistrar;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author far.liu
 */
public class ZookeeperRegistrarImpl implements IRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistrarImpl.class);
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final int SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 2;
    private static final String SEPARATOR = "/";

    private Map<String, List<String>> serviceProviderMap = new HashMap<String, List<String>>();
    private CuratorFramework curatorFramework;
    private ILoadbalance loadbalance;

    public void init(String registerAddress){
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(registerAddress)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
                .build();

        curatorFramework.start();

        loadbalance = LoadbalanceFactory.getLoadbalance();
    }

    public void register(String providerAddress, String service) {
        try {
            String servicePath = ZookeeperConfig.FOLDER + SEPARATOR + service;
            Stat stat = curatorFramework.checkExists().forPath(servicePath);
            if (stat == null) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            String provider = servicePath + SEPARATOR + providerAddress;

            curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(provider);
            logger.info("provider:{} is registered to {}", providerAddress, servicePath);
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
        }
    }


    public String discover(String service) {
        String path = ZookeeperConfig.FOLDER + SEPARATOR + service;
        try {
            List<String> provider = curatorFramework.getChildren().forPath(path);
            serviceProviderMap.put(service, provider);

            watchProvider(path);

           return loadbalance.select(serviceProviderMap.get(service));
        } catch (Exception e) {
            logger.error(String.format("call ZookeeperRegistrarImpl.discover, occur exception, service:[%s], e.getMessage:[%s]", service, e.getMessage()), e);
            return "";
        }
    }

    private void watchProvider(final String path) {
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                //提供者有更新，则及时更新到内存中
                serviceProviderMap.put(path, curatorFramework.getChildren().forPath(path));
            }
        };
        childrenCache.getListenable().addListener(listener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
