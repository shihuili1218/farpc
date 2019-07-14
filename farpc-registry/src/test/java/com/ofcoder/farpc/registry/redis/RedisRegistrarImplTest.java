package com.ofcoder.farpc.registry.redis;

import com.ofcoder.farpc.registry.IRegistrar;
import org.junit.Test;

/**
 * @author far.liu
 */
public class RedisRegistrarImplTest {

    @Test
    public void test(){
        IRegistrar registrar = new RedisRegistrarImpl();

        registrar.register("127.0.0.1:62880", "com.ofcoder.farpc.demo.api.IWelcome");
        registrar.discover("com.ofcoder.farpc.demo.api.IWelcome");
    }
}
