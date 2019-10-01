package com.mydubbo.registry.zookeeper;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:20
 * Desc:
 */
public class ZkRegistryDiscovery extends AbstractRegistryDiscovery {

    @Override
    public void save() {

    }

    @Override
    public Map<String, Map<URL, Class>> get() {
        return null;
    }
}
