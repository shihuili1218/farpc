package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.demo.api.IWelcome;
import com.ofcoder.farpc.rpc.anno.Provider;

/**
 * @author far.liu
 */
@Provider(interfaceClazz = IWelcome.class)
public class WelcomeImpl implements IWelcome {
    public String greet(String name) {
        return "hello " + name + ", welcome to ofcoder.";
    }
}
