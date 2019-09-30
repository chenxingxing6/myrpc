package com.mydubbo.registry.zookeeper;

import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:20
 * Desc:
 */
public class ZkRegistryDiscovery implements IRegistryDiscovery {
    @Override
    public void register(URL url, String interfaceName, Class implClass) {
        // TODO: 2019/9/30
    }

    @Override
    public Class discovery(URL url, String interfaceName) {
        // TODO: 2019/9/30
        return null;
    }

    @Override
    public URL randomServer(String interfaceName) {
        // TODO: 2019/9/30
        return null;
    }
}
