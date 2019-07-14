package com.ofcoder.farpc.registry;

import com.ofcoder.farpc.common.config.Property;
import com.ofcoder.farpc.common.loader.ExtensionLoader;

/**
 * @author: yuanyuan.liu
 * @date: 2019/6/25 14:46
 */
public class RegistrarFactory {

    public static IRegistrar getRegistrar() {
        String protocol = Property.Registry.protocol;
        IRegistrar extension = ExtensionLoader.getExtensionLoader(IRegistrar.class).getExtension(protocol);
        return extension;
    }
}
