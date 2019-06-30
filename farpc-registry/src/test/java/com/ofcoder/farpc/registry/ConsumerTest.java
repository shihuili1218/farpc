package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import org.junit.Test;

import java.io.IOException;

/**
 * @author far.liu
 */
public class ConsumerTest {
    @Test
    public void test() throws IOException {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        registrar.init("127.0.0.1:2181");
        System.out.println(registrar.discover("com.ofcoder.farpc.demo.api.IWelcome"));
    }
}
