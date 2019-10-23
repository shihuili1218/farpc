package com.ofcoder.farpc.rpc.netty;

import com.ofcoder.farpc.rpc.anno.Container;
import com.ofcoder.farpc.rpc.RequestDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author far.liu
 */
public class NettyProviderHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyProviderHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        RequestDTO requestDTO = (RequestDTO) msg;
        Object result = new Object();

        logger.info("receive request.. {}", requestDTO);
        if (Container.getProviders().containsKey(requestDTO.getClassName())) {
            Object provider = Container.getProviders().get(requestDTO.getClassName());

            Class<?> providerClazz = provider.getClass();
            Method method = providerClazz.getMethod(requestDTO.getMethodName(), requestDTO.getTypes());
            result = method.invoke(provider, requestDTO.getParams());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
}
