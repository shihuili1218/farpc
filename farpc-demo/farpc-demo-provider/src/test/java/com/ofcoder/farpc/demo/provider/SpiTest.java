package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.cluster.ILoadbalance;
import com.ofcoder.farpc.cluster.LoadbalanceFactory;
import com.ofcoder.farpc.common.loader.ExtensionLoader;
import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.RegistrarFactory;
import com.ofcoder.farpc.rpc.netty.RpcServer;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 15:07
 */
public class SpiTest {
    @Test
    public void start() throws IOException {
        RpcServer server = new RpcServer();
        server.init("127.0.0.1:20880");
        server.bind("com.ofcoder.farpc.demo.api.IWelcome",  new WelcomeImpl());
        server.publisher();
        System.in.read();
    }


    @Test
    public void test(){
        ILoadbalance loadbalance = LoadbalanceFactory.getLoadbalance();
        IRegistrar registrar = RegistrarFactory.getRegistrar();
        System.out.println();
    }

}
