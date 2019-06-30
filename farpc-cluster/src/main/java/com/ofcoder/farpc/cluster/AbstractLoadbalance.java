package com.ofcoder.farpc.cluster;

import java.util.List;

/**
 * @author far.liu
 */
public abstract class AbstractLoadbalance  implements ILoadbalance{
    public String select(List<String> providers) {
        if (providers == null || providers.size() == 0) {
            return null;
        }
        if (providers.size() == 1){
            return providers.get(0);
        }
        return doSelect(providers);
    }

    public abstract String doSelect(List<String> providers);
}
