package com.ofcoder.farpc.rpc.http;

import com.ofcoder.farpc.rpc.RequestDTO;
import com.ofcoder.farpc.rpc.anno.Container;
import com.ofcoder.farpc.rpc.netty.NettyProviderHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

/**
 * @author far.liu
 */
public class HttpServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    public void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ServletInputStream inputStream = req.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RequestDTO requestDTO = (RequestDTO) objectInputStream.readObject();

            Object result = new Object();

            logger.info("receive request.. {}", requestDTO);
            if (Container.getProviders().containsKey(requestDTO.getClassName())) {
                Object provider = Container.getProviders().get(requestDTO.getClassName());

                Class<?> providerClazz = provider.getClass();
                Method method = providerClazz.getMethod(requestDTO.getMethodName(), requestDTO.getTypes());
                result = method.invoke(provider, requestDTO.getParams());
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
            objectOutputStream.close();
//            IOUtils.write(result.toString(), resp.getOutputStream());
        } catch (Exception e) {
        }
    }
}
