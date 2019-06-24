package com.ofcoder.farpc.registry;

/**
 * @author far.liu
 */
public class ConsumerTest {
    public static void main(String[] args) {
        IRegistrar registrar = new ZookeeperRegistrarImpl();
        System.out.println(registrar.discover("com.ofcoder.farpc.IInteresting"));
    }
}
