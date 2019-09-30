package com.mydubbo.config;

import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.registry.localfile.FileRegisterDiscovery;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:23
 * Desc: 服务注册方式
 */
public enum  RegistryEnum {
    LOCALFILE("localfile", new FileRegisterDiscovery()),
    ZOOKEEPER("zookeeper", null),
    REDIS("redis", null);

    RegistryEnum(String key, IRegistryDiscovery registryDiscovery){
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

    private IRegistryDiscovery registryDiscovery;
    private String key;

    public IRegistryDiscovery getRegistryDiscovery() {
        return registryDiscovery;
    }

    public void setRegistryDiscovery(IRegistryDiscovery registryDiscovery) {
        this.registryDiscovery = registryDiscovery;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
