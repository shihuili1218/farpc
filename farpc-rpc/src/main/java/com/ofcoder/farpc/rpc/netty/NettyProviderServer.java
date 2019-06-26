package com.ofcoder.farpc.rpc.netty;

import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.RegistrarFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author far.liu
 */
public class NettyProviderServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyProviderServer.class);
    private IRegistrar registrar;
    private String selfAddress;
    private Map<String, Object> providers = new HashMap<String, Object>();

    public void init(String selfAddress) {
        this.registrar = RegistrarFactory.getRegistrar();
        this.selfAddress = selfAddress;
    }

    public void bind(String interfaceName, Object provider) {
        providers.put(interfaceName, provider);
        //TODO scanning annotation
    }

    public void publisher() {

        // 服务注册，发布
        for (String service : providers.keySet()) {
            registrar.register(selfAddress, service);
        }

        // 启动服务
        try {
//            EventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("qos-boss", true));
//            EventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("qos-worker", true));
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,4,10,0));
//                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyProviderServer.class.getClassLoader())));
                            pipeline.addLast(new NettyProviderHandler(providers));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] addrs = selfAddress.split(":");
            String ip = addrs[0];
            Integer port = Integer.parseInt(addrs[1]);
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            logger.info("netty server is started...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
