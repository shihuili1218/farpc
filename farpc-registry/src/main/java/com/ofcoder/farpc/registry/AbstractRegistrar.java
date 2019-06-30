package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.cluster.ILoadbalance;
import com.ofcoder.farpc.cluster.LoadbalanceFactory;

import java.util.List;

/**
 * @author far.liu
 */
public abstract class AbstractRegistrar implements IRegistrar {
    public String discover(String service) {
        List<String> providers = lookup(service);
        ILoadbalance loadbalance = LoadbalanceFactory.getLoadbalance();
        String select = loadbalance.select(providers);
        return select;
    }

    public abstract List<String> lookup(String service);
}
