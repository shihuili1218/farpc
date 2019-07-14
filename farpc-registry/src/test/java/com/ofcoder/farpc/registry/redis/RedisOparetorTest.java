package com.ofcoder.farpc.registry.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author far.liu
 */
public class RedisOparetorTest {

    @Test
    public void test(){
        RedisOparetor.init("127.0.0.1:6379");
        Jedis jedis = RedisOparetor.getJedis();
        jedis.lpush("farpc", "com.ofcoder.farpc.demo.api.IWelcome");

    }
}
