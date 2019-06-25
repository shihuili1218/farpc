package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.demo.api.IWelcome;
import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.zookeeper.ZookeeperRegistrarImpl;
import com.ofcoder.farpc.rpc.netty.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author far.liu
 */
public class ProviderTest {
    private static final Logger logger = LoggerFactory.getLogger(ProviderTest.class);

    public static void main(String[] args) throws IOException {
        logger.info("hello guys.");
        IWelcome welcome = new WelcomeImpl();
        IRegistrar registrar = new ZookeeperRegistrarImpl();

        RpcServer server = new RpcServer(registrar, "127.0.0.1:20880");
        server.bind("com.ofcoder.farpc.demo.api.IWelcome", welcome);
        server.publisher();
        System.in.read();
    }
}
