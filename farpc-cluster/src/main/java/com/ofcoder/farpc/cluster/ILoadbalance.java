package com.ofcoder.farpc.cluster;

import java.util.List;

/**
 * @author far.liu
 */
public interface ILoadbalance {

    String select(List<String> providers);
}
