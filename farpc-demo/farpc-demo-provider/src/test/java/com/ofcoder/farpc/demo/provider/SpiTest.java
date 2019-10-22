package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.cluster.ILoadbalance;
import com.ofcoder.farpc.cluster.LoadbalanceFactory;
import com.ofcoder.farpc.common.loader.ExtensionLoader;
import com.ofcoder.farpc.demo.api.IWelcome;
import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.RegistrarFactory;
import com.ofcoder.farpc.rpc.netty.NettyConsumerProxy;
import com.ofcoder.farpc.rpc.netty.NettyProviderServer;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 15:07
 */
public class SpiTest {
    @Test
    public void providerTest() throws IOException {
        NettyProviderServer server = new NettyProviderServer();
        server.start("127.0.0.1:20880");
        System.in.read();
    }

    @Test
    public void consumerTest(){
        IRegistrar registrar = RegistrarFactory.getRegistrar();
        IWelcome welcome = NettyConsumerProxy.create(registrar, IWelcome.class);
        String far = welcome.greet("far");
        System.out.println(far);
    }

    @Test
    public void test() {
        ILoadbalance loadbalance = LoadbalanceFactory.getLoadbalance();
        IRegistrar registrar = RegistrarFactory.getRegistrar();
        System.out.println();
    }

    @Test
    public void spiTest(){
        ILoadbalance round = ExtensionLoader.getExtensionLoader(ILoadbalance.class)
                .getExtension("round");
        ILoadbalance random = ExtensionLoader.getExtensionLoader(ILoadbalance.class)
                .getExtension("random");
        System.out.println(round.getClass().getName());
        System.out.println(random.getClass().getName());
    }

}
