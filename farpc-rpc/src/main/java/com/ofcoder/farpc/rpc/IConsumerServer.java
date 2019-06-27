package com.ofcoder.farpc.rpc;

import io.netty.channel.Channel;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/27 9:24
 */
public interface IConsumerServer {
    void open();

    void connect();

    Channel getChannel();

}
