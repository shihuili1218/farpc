package com.ofcoder.farpc.demo.provider;

import com.ofcoder.farpc.demo.api.IWelcome;

/**
 * @author far.liu
 */
public class WelcomeImpl implements IWelcome {
    public String greet(String name) {
        return "hello " + name + ", welcome to ofcoder.";
    }
}
