package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.demo.api.IWelcome;
import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import com.ofcoder.farpc.rpc.netty.RpcServer;
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
        registrar.init("192.168.1.118:2181");

        RpcServer server = new RpcServer();
        server.init("127.0.0.1:20880");
        server.bind("com.ofcoder.farpc.demo.api.IWelcome", welcome);
        server.publisher();
        System.in.read();
    }

}
