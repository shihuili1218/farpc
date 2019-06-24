package com.ofcoder.farpc.cluster;

import java.util.List;
import java.util.Random;

/**
 * @author far.liu
 */
public class RandomLoadbalanceImpl implements ILoadbalance {
    public String select(List<String> providers) {
        int len = providers.size();
        Random random = new Random();
        int lucky = random.nextInt(len);
        return providers.get(lucky);
    }
}
