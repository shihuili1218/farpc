package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.common.anno.FarSPI;

/**
 * @author far.liu
 */
@FarSPI("zookeeper")
public interface IRegistrar {

    void init(String registerAddress);

    void register(String providerAddress, String service);

    String discover(String service);
}
