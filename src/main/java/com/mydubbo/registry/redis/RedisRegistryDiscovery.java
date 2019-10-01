package com.mydubbo.registry.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.util.RedisUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:19
 * Desc:
 */
public class RedisRegistryDiscovery extends AbstractRegistryDiscovery {
    private static final String key = "redis:registry:server";

    @Override
    public void save() {
        RedisUtil.setObject(key, REGISTER);

    }

    @Override
    public Map<String, Map<URL, Class>> get() {
        Map<String, Map<URL, Class>> result = (Map)RedisUtil.getObject(key);
        return result;
    }
}
