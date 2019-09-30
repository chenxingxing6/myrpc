package com.mydubbo.registry;

import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:09
 * Desc: 服务注册，发现接口
 */
public interface IRegistryDiscovery {

    /**
     * 服务注册
     * @param url
     * @param interfaceName
     * @param implClass
     */
    public void register(URL url, String interfaceName, Class implClass);

    /**
     * 服务发现
     * @param url
     * @param interfaceName
     * @return
     */
    public Class discovery(final URL url, String interfaceName);

    /**
     * 负载均衡，获取可用服务地址
     * @param interfaceName
     * @return
     */
    public URL randomServer(String interfaceName);
}
