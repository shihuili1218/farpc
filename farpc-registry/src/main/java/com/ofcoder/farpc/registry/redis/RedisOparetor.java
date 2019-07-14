package com.ofcoder.farpc.registry.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author far.liu
 */
public class RedisOparetor {
    private static Logger logger = LoggerFactory.getLogger(RedisOparetor.class);
    private static int MAX_ACTIVE = 1024;
    private static int MAX_IDLE = 200;
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    private static boolean TEST_ON_BORROW = true;
    private static JedisPool jedisPool = null;

    public static void init(String addr) {
        if (jedisPool == null) {
            synchronized (RedisOparetor.class) {
                if (jedisPool == null) {
                    try {
                        String[] split = addr.split(":");
                        String host = split[0];
                        Integer port = Integer.parseInt(split[1]);
                        JedisPoolConfig config = new JedisPoolConfig();
                        config.setMaxTotal(MAX_ACTIVE);
                        config.setMaxIdle(MAX_IDLE);
                        config.setMaxWaitMillis(MAX_WAIT);
                        config.setTestOnBorrow(TEST_ON_BORROW);
                        jedisPool = new JedisPool(config, host, port, TIMEOUT);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                throw new RuntimeException("call RedisOparetor.getJedis, connection is closed");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /***
     * 释放资源
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}