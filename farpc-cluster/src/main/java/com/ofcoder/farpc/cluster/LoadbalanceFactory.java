package com.ofcoder.farpc.cluster;

import com.ofcoder.farpc.common.config.Property;
import com.ofcoder.farpc.common.loader.ExtensionLoader;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 14:26
 */
public class LoadbalanceFactory {

    public static ILoadbalance getLoadbalance() {
        String loadbalance = Property.Cluster.loadbalance;
        ILoadbalance extension = ExtensionLoader.getExtensionLoader(ILoadbalance.class).getExtension(loadbalance);
        return extension;
    }
}
