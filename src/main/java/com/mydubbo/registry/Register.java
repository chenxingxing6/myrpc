package com.mydubbo.registry;

import com.mydubbo.rpc.framework.URL;

import java.util.*;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:19
 * Desc: 注册中心
 */
public class Register {
    private static Map<String/*接口名*/, Map<URL, Class>> REGISTER = new HashMap<String, Map<URL, Class>>();

    /**
     * 服务注册
     * @param url
     * @param interfaceName
     * @param implClass
     */
    public static void registServer(URL url, String interfaceName, Class implClass){
        Map<URL, Class> map = new HashMap<URL, Class>();
        map.put(url, implClass);
        REGISTER.put(interfaceName, map);
    }

    /**
     * 发现服务
     * @param url
     * @param interfaceName
     * @return
     */
    public static Class findServer(final URL url, String interfaceName){
        return Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.get(url)).orElse(null);
    }

    /**
     * 随机获取服务，模拟负载均衡（后面有空实现）
     * @param interfaceName
     * @return
     */
    public static URL randomServer(String interfaceName){
        Set<URL> urls = REGISTER.get(interfaceName).keySet();
        if (urls.isEmpty()){
            return null;
        }
        return urls.iterator().next();
    }
}
