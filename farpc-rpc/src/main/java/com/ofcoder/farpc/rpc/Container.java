package com.ofcoder.farpc.rpc;

import com.ofcoder.farpc.registry.IRegistrar;
import com.ofcoder.farpc.registry.RegistrarFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author far.liu
 */
public class Container {
    private static final Logger logger = LoggerFactory.getLogger(Container.class);
    private static IRegistrar registrar = RegistrarFactory.getRegistrar();
    private static Map<String, Object> providers = new HashMap<String, Object>();

    static {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.ofcoder"))
                .setScanners(new TypeAnnotationsScanner()));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Provider.class, true);
        for (Class<?> clazz : classes) {
            try {
                Provider annotation = clazz.getAnnotation(Provider.class);
                Object provider = clazz.newInstance();
                String canonicalName = annotation.interfaceClazz().getCanonicalName();
                providers.put(canonicalName, provider);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    public static void registerSelf(String selfAddress){
        for (String service : providers.keySet()) {
            registrar.register(selfAddress, service);
        }
    }

    public static Map<String, Object> getProviders() {
        return providers;
    }
}
