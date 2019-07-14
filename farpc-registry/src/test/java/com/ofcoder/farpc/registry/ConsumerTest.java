package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.registry.redis.RedisRegistrarImpl;
import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import org.junit.Test;

import java.io.IOException;

/**
 * @author far.liu
 */
public class ConsumerTest {
    @Test
    public void zkTest() throws IOException {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        System.out.println(registrar.discover("com.ofcoder.farpc.demo.api.IWelcome"));
    }

    @Test
    public void test() throws IOException {
        IRegistrar registrar = new RedisRegistrarImpl();
        System.out.println(registrar.discover("com.ofcoder.farpc.demo.api.IWelcome"));
    }
}
