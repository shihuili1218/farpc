package com.ofcoder.farpc.rpc.netty;

import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.rpc.RequestDTO;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author far.liu
 */
public class NettyConsumerProxy {
    private static final Logger logger = LoggerFactory.getLogger(NettyConsumerProxy.class);


    public static <T> T create(final IRegistrar registrar, final Class<T> interfaceClass) {
        Object o = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        RequestDTO requestDTO = new RequestDTO();
                        requestDTO.setClassName(method.getDeclaringClass().getName());
                        requestDTO.setMethodName(method.getName());
                        requestDTO.setTypes(method.getParameterTypes());
                        requestDTO.setParams(args);

                        String service = interfaceClass.getName();
                        String serivceAddress = registrar.discover(service);
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
                                    .handler(new ChannelInitializer<SocketChannel>() {
                                        @Override
                                        protected void initChannel(SocketChannel channel) throws Exception {
                                            ChannelPipeline pipeline = channel.pipeline();
                                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
                                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.softCachingResolver(NettyProviderServer.class.getClassLoader())));
                                            pipeline.addLast("encoder", new ObjectEncoder());
                                            pipeline.addLast(consumerHandler);
                                        }
                                    });
                            ChannelFuture future = bootstrap.connect(host, port).sync();

                            future.channel().writeAndFlush(requestDTO);
                            future.channel().closeFuture().sync();
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        } finally {
                            group.shutdownGracefully();
                        }
                        return consumerHandler.getResponse();
                    }
                });
        return (T) o;
    }
}
