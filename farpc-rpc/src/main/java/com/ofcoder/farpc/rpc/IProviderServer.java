package com.ofcoder.farpc.rpc;

import com.ofcoder.farpc.common.anno.FarSPI;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/27 9:25
 */
@FarSPI("netty")
public interface IProviderServer {

    void start(String selfAddress);

}
