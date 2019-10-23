package com.ofcoder.farpc.rpc.netty;

import com.ofcoder.farpc.rpc.ConsumerProxy;
import com.ofcoder.farpc.rpc.IConsumerServer;
import com.ofcoder.farpc.rpc.RequestDTO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author far.liu
 */
public class NettyConsumerServer implements IConsumerServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyConsumerServer.class);

    public Object execute(String serivceAddress, RequestDTO requestDTO) {
        String[] addrs = serivceAddress.split(":");
        String host = addrs[0];
        Integer port = Integer.parseInt(addrs[1]);

        final NettyConsumerHandler consumerHandler = new NettyConsumerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast( new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(ConsumerProxy.class.getClassLoader())));
                            pipeline.addLast( new ObjectEncoder());
                            pipeline.addLast(consumerHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();

            Channel channel = future.channel();
            channel.writeAndFlush(requestDTO);
            logger.info("send request..., {}", requestDTO);
            channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
        return consumerHandler.getResponse();

    }
}
