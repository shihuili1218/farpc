package com.ofcoder.farpc.registry.redis;

import com.ofcoder.farpc.registry.AbstractRegistrar;

import java.util.List;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 15:46
 */
public class RedisRegistrarImpl extends AbstractRegistrar {

    public void register(String providerAddress, String service) {
        RedisOparetor.getJedis().lpush(FOLDER + SEPARATOR + service, providerAddress);
    }

    @Override
    protected void init(String address) {
        RedisOparetor.init(address);
    }

    @Override
    public List<String> lookup(String service) {
        List<String> lrange = RedisOparetor.getJedis().lrange(FOLDER + SEPARATOR + service, 0, -1);
        return lrange;
    }
}
