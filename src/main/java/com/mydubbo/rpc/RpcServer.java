package com.mydubbo.rpc;

import com.mydubbo.config.ProtocolConfig;
import com.mydubbo.config.ProtocolEnum;
import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.registry.RegistryDiscoveryFactory;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolServer;

/**
 * User: lanxinghua
 * Date: 2019/9/30 15:48
 * Desc:
 */
public class RpcServer {
    // 配置文件
    private ProtocolConfig config;
    // 服务注册发现
    private IRegistryDiscovery registryDiscovery;


    public RpcServer(ProtocolConfig config, RegistryDiscoveryFactory registryDiscoveryFactory) {
        this.config = config;
        this.registryDiscovery = registryDiscoveryFactory.getRegistryDiscovery();
    }

    /**
     * 启动服务
     */
    public void start(){
        String configName = config.name;
        String host = config.host;
        Integer port = config.port;
        String charset = config.charset;
        ProtocolEnum protocolEnum = ProtocolEnum.getInstance(configName);
        if (protocolEnum == null){
            throw new RuntimeException("传输协议配置有误");
        }
        IProtocolServer service = protocolEnum.getProtocolService();
        service.start(new URL(host, port), charset, registryDiscovery);
    }

    /**
     * 服务注册
     * @param interfaceName
     * @param implClass
     */
    public void register(String interfaceName, Class implClass){
        System.out.println("服务注册" + String.format("%s  %s:%s", interfaceName, config.host, config.port));
        registryDiscovery.register(new URL(config.host, config.port), interfaceName, implClass);
    }

    /**
     * 关闭服务
     */
    public void close(){
        System.exit(0);
    }
}
