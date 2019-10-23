package com.ofcoder.farpc.rpc;

import com.ofcoder.farpc.common.anno.FarSPI;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/27 9:24
 */
@FarSPI("netty")
public interface IConsumerServer {
    Object execute(String address, RequestDTO requestDTO);
}
