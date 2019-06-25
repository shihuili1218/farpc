package com.ofcoder.farpc.cluster;

import com.ofcoder.farpc.common.anno.FarSPI;

import java.util.List;

/**
 * @author far.liu
 */
@FarSPI("random")
public interface ILoadbalance {

    String select(List<String> providers);
}
