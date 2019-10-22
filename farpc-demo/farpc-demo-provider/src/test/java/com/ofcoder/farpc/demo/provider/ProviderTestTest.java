package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.demo.api.IWelcome;
import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.RegistrarFactory;
import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import com.ofcoder.farpc.rpc.netty.NettyProviderServer;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 15:02
 */
public class ProviderTestTest {
    @Test
    public void test() throws IOException {
        IWelcome welcome = new WelcomeImpl();
        IRegistrar registrar = new ZookeeperRegistrarImpl();

        NettyProviderServer server = new NettyProviderServer();
        server.start("127.0.0.1:20880");
        System.in.read();
    }

    @Test
    public void spiTest() throws IOException {
        IRegistrar registrar = RegistrarFactory.getRegistrar();
        registrar.register("127.0.0.1:62880", IWelcome.class.getName());
        System.in.read();
    }
}
