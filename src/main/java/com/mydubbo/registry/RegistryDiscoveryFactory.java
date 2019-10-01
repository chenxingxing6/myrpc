package com.mydubbo.registry;

import com.mydubbo.config.RegistryEnum;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:29
 * Desc: 注册发现工厂，获取注册发现Bean
 */
public class RegistryDiscoveryFactory {
    // 服务注册方式,默认为本地文件
    private String registryType = RegistryEnum.LOCALFILE.getKey();

    public RegistryDiscoveryFactory(String registryType) {
        this.registryType = registryType;
    }

    public AbstractRegistryDiscovery getRegistryDiscovery(){
        RegistryEnum registryEnum = RegistryEnum.getInstance(registryType);
        if (registryEnum == null){
            throw new RuntimeException("服务注册方式不存在");
        }
        return registryEnum.getRegistryDiscovery();
    }
}
