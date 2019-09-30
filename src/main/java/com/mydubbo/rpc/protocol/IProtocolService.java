package com.mydubbo.rpc.protocol;

import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 17:24
 * Desc:
 */
public interface IProtocolService {
    /**
     * 启动服务
     * @param url
     * @param charset
     * @param registryDiscovery
     */
    public void start(URL url, String charset, IRegistryDiscovery registryDiscovery);
}
