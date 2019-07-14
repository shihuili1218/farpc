package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.cluster.ILoadbalance;
import com.ofcoder.farpc.cluster.LoadbalanceFactory;
import com.ofcoder.farpc.common.config.Property;

import java.util.List;

/**
 * @author far.liu
 */
public abstract class AbstractRegistrar implements IRegistrar {
    protected static final String FOLDER = "/faregistrys";
    protected static final String SEPARATOR = "/";

    public AbstractRegistrar() {
        String address = Property.Registry.address;
        init(address);
    }

    public String discover(String service) {
        List<String> providers = lookup(service);
        ILoadbalance loadbalance = LoadbalanceFactory.getLoadbalance();
        String select = loadbalance.select(providers);
        return select;
    }

    protected abstract void init(String address);

    protected abstract List<String> lookup(String service);
}
