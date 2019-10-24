package com.ofcoder.farpc.rpc;

import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.RegistrarFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author far.liu
 */
public class ConsumerProxy {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerProxy.class);

    public static <T> T create(final Class<T> interfaceClass) {
        Object o = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        // 封装调用参数
                        RequestDTO requestDTO = new RequestDTO();
                        requestDTO.setClassName(method.getDeclaringClass().getName());
                        requestDTO.setMethodName(method.getName());
                        requestDTO.setTypes(method.getParameterTypes());
                        requestDTO.setParams(args);

                        // 获取服务提供者
                        IRegistrar registrar = RegistrarFactory.getRegistrar();
                        String service = interfaceClass.getName();
                        String serivceAddress = registrar.discover(service);

                        // 调用上一章写的远程调用
                        Object result = RpcFactory.getConsumerService().execute(serivceAddress, requestDTO);
                        return result;
                    }
                });
        return (T) o;
    }
}
