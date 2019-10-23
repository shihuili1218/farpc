package com.ofcoder.farpc.rpc;

import org.junit.Test;

import java.io.IOException;

/**
 * @author far.liu
 */
public class RpcFactoryTest {

    @Test
    public void consumer() {
        IConsumerServer consumerService = RpcFactory.getConsumerService();
        Object execute = consumerService.execute("127.0.0.1:20880", new RequestDTO());
    }

    @Test
    public void provider() throws IOException {
        IProviderServer providerServer = RpcFactory.getProviderServer();
        providerServer.start("127.0.0.1:20880");
        System.in.read();

    }
}
