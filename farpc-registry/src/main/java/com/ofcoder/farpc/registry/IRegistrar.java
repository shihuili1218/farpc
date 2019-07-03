package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.common.anno.FarSPI;

/**
 * @author far.liu
 */
@FarSPI("zookeeper")
public interface IRegistrar {

    /**
     * 初始化
     * @param registerAddress zookeeper地址，例如127.0.0.1:2181
     */
    void init(String registerAddress);

    /**
     * 注册服务
     * @param providerAddress 服务提供者地址
     * @param service 服务
     */
    void register(String providerAddress, String service);

    String discover(String service);
}
