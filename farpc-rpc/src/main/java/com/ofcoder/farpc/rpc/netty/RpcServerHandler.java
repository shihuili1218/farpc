package com.ofcoder.farpc.rpc.netty;

import com.ofcoder.farpc.rpc.RequestDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author far.liu
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> providers;

    public RpcServerHandler(Map<String, Object> providers) {
        this.providers = providers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        RequestDTO requestDTO = (RequestDTO) msg;
        Object result = new Object();

        if (providers.containsKey(requestDTO.getClassName())) {
            Object provider = providers.get(requestDTO.getClassName());

            Class<?> providerClazz = provider.getClass();
            Method method = providerClazz.getMethod(requestDTO.getMethodName(), requestDTO.getTypes());
            result = method.invoke(providerClazz, requestDTO.getParams());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
}
