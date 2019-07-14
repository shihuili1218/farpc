package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.registry.redis.RedisRegistrarImpl;
import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import org.junit.Test;

import java.io.IOException;

/**
 * @author far.liu
 */
public class ProviderTest {

    @Test
    public void zkTest() throws IOException {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        registrar.register("127.0.0.1:20880", "com.ofcoder.farpc.demo.api.IWelcome");
        System.in.read();
    }

    @Test
    public void redisTest() throws IOException {
        IRegistrar registrar = new RedisRegistrarImpl();
        registrar.register("127.0.0.1:20880", "com.ofcoder.farpc.demo.api.IWelcome");
        System.in.read();
    }
}
