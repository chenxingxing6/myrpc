package com.mydubbo.registry.redis;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:19
 * Desc:
 */
public class RedisRegistryDiscovery extends AbstractRegistryDiscovery {

    @Override
    public void save() {

    }

    @Override
    public Map<String, Map<URL, Class>> get() {
        return null;
    }
}
