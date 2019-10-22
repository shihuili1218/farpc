package com.ofcoder.farpc.rpc.netty;

import com.ofcoder.farpc.rpc.Container;
import com.ofcoder.farpc.rpc.IProviderServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author far.liu
 */
public class NettyProviderServer implements IProviderServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyProviderServer.class);

    public void start(String selfAddress) {
        Container.registerSelf(selfAddress);

        String[] addrs = selfAddress.split(":");
        String ip = addrs[0];
        Integer port = Integer.parseInt(addrs[1]);

        publisher(ip, port);
    }

    private void publisher(String ip, Integer port) {
        // 启动服务
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyProviderServer.class.getClassLoader())));
                            pipeline.addLast(new NettyProviderHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(ip, port).sync();
            logger.info("netty server is started...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
