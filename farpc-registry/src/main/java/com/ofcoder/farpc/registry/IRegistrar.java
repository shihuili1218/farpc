package com.ofcoder.farpc.registry;

/**
 * @author far.liu
 */
public interface IRegistrar {

    void register(String providerAddress, String service);

    String discover(String service);
}
