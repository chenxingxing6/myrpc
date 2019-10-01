package com.mydubbo.registry;

import com.mydubbo.rpc.framework.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:09
 * Desc: 服务注册，发现接口
 */
public abstract class AbstractRegistryDiscovery {
    protected static Map<String/*接口名*/, Map<URL, Class>> REGISTER = new HashMap<String, Map<URL, Class>>();

    /**
     * 服务注册
     * @param url
     * @param interfaceName
     * @param implClass
     */
    public void register(URL url, String interfaceName, Class implClass){
        Map<URL, Class> map = new HashMap<URL, Class>();
        map.put(url, implClass);
        REGISTER.put(interfaceName, map);
        save();
    }

    /**
     * 服务发现
     * @param url
     * @param interfaceName
     * @return
     */
    public Class discovery(final URL url, String interfaceName){
        REGISTER = get();
        return Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.get(url)).orElseThrow(() -> new RuntimeException("service not found!"));
    }

    /**
     * 负载均衡，获取可用服务地址
     * @param interfaceName
     * @return
     */
    public URL randomServer(String interfaceName){
        REGISTER = get();
        Set<URL> urls = Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.keySet()).orElseThrow(() -> new RuntimeException("service not found!"));
        if (urls == null || urls.isEmpty()){
            throw new RuntimeException("service not found!");
        }
        // 这里就返回第一个
        return urls.iterator().next();
    }

    /**
     * 保存到注册中心
     */
    public abstract void save();

    /**
     * 从注册中心获取
     */
    public abstract Map<String, Map<URL, Class>> get();
}
