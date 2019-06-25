package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.rpc.netty.RpcServer;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 15:07
 */
public class SpiTest {
    @Test
    public void test() throws IOException {
        RpcServer server = new RpcServer();
        server.init("127.0.0.1:20880");
        server.bind("com.ofcoder.farpc.demo.api.IWelcome",  new WelcomeImpl());
        server.publisher();
        System.in.read();
    }
}
