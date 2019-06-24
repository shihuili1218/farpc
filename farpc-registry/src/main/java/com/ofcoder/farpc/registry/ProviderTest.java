package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;

import java.io.IOException;

/**
 * @author far.liu
 */
public class ProviderTest {
    public static void main(String[] args) throws IOException {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        registrar.register("127.0.0.1:20880", "com.ofcoder.farpc.IInteresting");
        System.in.read();
    }
}
