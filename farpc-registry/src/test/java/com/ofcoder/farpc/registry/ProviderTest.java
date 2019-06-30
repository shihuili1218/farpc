package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import org.junit.Test;

import java.io.IOException;

/**
 * @author far.liu
 */
public class ProviderTest {

    @Test
    public void test() throws IOException {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        registrar.init("127.0.0.1:2181");
        registrar.register("127.0.0.1:20880", "com.ofcoder.farpc.demo.api.IWelcome");
        System.in.read();
    }
}
