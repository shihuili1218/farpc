package com.ofcoder.farpc.rpc;

import com.ofcoder.farpc.common.config.Property;
import com.ofcoder.farpc.common.loader.ExtensionLoader;

/**
 * @author far.liu
 */
public class RpcFactory {
    public static IConsumerServer getConsumerService() {
        String protocol = Property.Rpc.protocol;
        IConsumerServer extension = ExtensionLoader.getExtensionLoader(IConsumerServer.class).getExtension(protocol);
        return extension;
    }

    public static IProviderServer getProviderServer() {
        String protocol = Property.Rpc.protocol;
        IProviderServer extension = ExtensionLoader.getExtensionLoader(IProviderServer.class).getExtension(protocol);
        return extension;
    }
}
