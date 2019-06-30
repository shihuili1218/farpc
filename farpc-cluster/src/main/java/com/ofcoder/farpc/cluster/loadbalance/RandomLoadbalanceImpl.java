package com.ofcoder.farpc.cluster.loadbalance;

import com.ofcoder.farpc.cluster.AbstractLoadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author far.liu
 */
public class RandomLoadbalanceImpl extends AbstractLoadbalance {
    @Override
    public String doSelect(List<String> providers) {
        int len = providers.size();
        Random random = new Random();
        int lucky = random.nextInt(len);
        return providers.get(lucky);
    }
}
