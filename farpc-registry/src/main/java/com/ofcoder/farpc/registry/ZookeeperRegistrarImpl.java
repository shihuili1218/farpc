package com.ofcoder.farpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author far.liu
 */
public class ZookeeperRegistrarImpl implements IRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistrarImpl.class);
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final int SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 2;
    public static final String SEPARATOR = "/";

    private static CuratorFramework curatorFramework;

    static {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZookeeperConfig.ADDRESS)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
                .build();

        curatorFramework.start();
    }

    public void regsiter(String providerAddress, String service) {
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
}
