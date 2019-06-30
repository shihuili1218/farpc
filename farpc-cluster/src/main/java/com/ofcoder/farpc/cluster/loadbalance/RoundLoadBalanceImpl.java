package com.ofcoder.farpc.cluster.loadbalance;

import com.ofcoder.farpc.cluster.AbstractLoadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author far.liu
 */
public class RoundLoadBalanceImpl extends AbstractLoadbalance {
    private AtomicInteger previous = new AtomicInteger(0);

    @Override
    public String doSelect(List<String> providers) {
        int size = providers.size();
        if (previous.get() >= size) {
            previous.set(0);
        }
        String provider = providers.get(previous.get());
        previous.set(previous.get() + 1);
        return provider;
    }
}
