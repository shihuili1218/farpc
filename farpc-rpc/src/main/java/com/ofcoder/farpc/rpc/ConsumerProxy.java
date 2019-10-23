package com.ofcoder.farpc.rpc;

import com.ofcoder.farpc.registry.IRegistrar;
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

                        Object result = RpcFactory.getConsumerService().execute(serivceAddress, requestDTO);
                        return result;
                    }
                });
        return (T) o;
    }
}
