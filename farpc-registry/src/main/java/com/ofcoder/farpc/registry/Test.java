package com.ofcoder.farpc.registry;

import java.io.IOException;

/**
 * @author far.liu
 */
public class Test {
    public static void main(String[] args) throws IOException {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        registrar.register("127.0.0.1:20880", "com.ofcoder.IInteresting");
        System.in.read();
    }
}
