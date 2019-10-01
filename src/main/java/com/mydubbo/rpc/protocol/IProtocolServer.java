package com.mydubbo.rpc.protocol;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 17:24
 * Desc:
 */
public interface IProtocolServer {
    /**
     * 启动服务
     * @param url
     * @param charset
     * @param registryDiscovery
     */
    public void start(URL url, String charset, AbstractRegistryDiscovery registryDiscovery);
}
