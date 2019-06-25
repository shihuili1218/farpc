package com.ofcoder.farpc.common.config;

import com.ofcoder.farpc.common.uitl.PropertyUtil;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 14:16
 */
public class Property {
    private static String REGISTRY_PROTOCOL_KEY = "farpc.registry.protocol";
    private static String REGISTRY_ADDRESS_KEY = "farpc.registry.address";
    private static String CLUSTER_LOADBALANCE_KEY = "farpc.cluster.loadbalance";

    static {
        Registry.protocol = PropertyUtil.getInstance().get(REGISTRY_PROTOCOL_KEY);
        Registry.address = PropertyUtil.getInstance().get(REGISTRY_ADDRESS_KEY);
        Cluster.loadbalance = PropertyUtil.getInstance().get(CLUSTER_LOADBALANCE_KEY);
    }

    public static class Registry {
        public static String protocol;
        public static String address;
    }

    public static class Cluster {
        public static String loadbalance;
    }


}
