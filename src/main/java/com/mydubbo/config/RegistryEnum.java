package com.mydubbo.config;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.registry.localfile.FileRegisterDiscovery;
import com.mydubbo.registry.redis.RedisRegistryDiscovery;
import com.mydubbo.registry.zookeeper.ZkRegistryDiscovery;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:23
 * Desc: 服务注册方式
 */
public enum  RegistryEnum {
    LOCALFILE("localfile", new FileRegisterDiscovery()),
    ZOOKEEPER("zookeeper", new ZkRegistryDiscovery()),
    REDIS("redis", new RedisRegistryDiscovery());

    RegistryEnum(String key, AbstractRegistryDiscovery registryDiscovery){
        this.key = key;
        this.registryDiscovery = registryDiscovery;
    }

    public static RegistryEnum getInstance(String key){
        for (RegistryEnum value : RegistryEnum.values()) {
            if (value.key.equalsIgnoreCase(key.trim())){
                return value;
            }
        }
        return null;
    }

    private AbstractRegistryDiscovery registryDiscovery;
    private String key;

    public AbstractRegistryDiscovery getRegistryDiscovery() {
        return registryDiscovery;
    }

    public void setRegistryDiscovery(AbstractRegistryDiscovery registryDiscovery) {
        this.registryDiscovery = registryDiscovery;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
