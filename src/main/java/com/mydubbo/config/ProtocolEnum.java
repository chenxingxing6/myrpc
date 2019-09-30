package com.mydubbo.config;

import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.registry.localfile.FileRegisterDiscovery;
import com.mydubbo.rpc.protocol.IProtocolClient;
import com.mydubbo.rpc.protocol.IProtocolService;
import com.mydubbo.rpc.protocol.http.HttpClient;
import com.mydubbo.rpc.protocol.http.HttpServer;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:23
 * Desc: 服务调用协议
 */
public enum ProtocolEnum {
    SOCKET("socket", null, null),
    HTTP("http", new HttpClient(), new HttpServer()),
    DUBBO("dubbo", null, null);

    ProtocolEnum( String key, IProtocolClient protocolClient, IProtocolService protocolService) {
        this.protocolClient = protocolClient;
        this.protocolService = protocolService;
        this.key = key;
    }

    public static ProtocolEnum getInstance(String key){
        for (ProtocolEnum value : ProtocolEnum.values()) {
            if (value.key.equalsIgnoreCase(key.trim())){
                return value;
            }
        }
        return null;
    }

    // 客户端
    private IProtocolClient protocolClient;
    // 服务端
    private IProtocolService protocolService;
    private String key;

    public IProtocolClient getProtocolClient() {
        return protocolClient;
    }

    public void setProtocolClient(IProtocolClient protocolClient) {
        this.protocolClient = protocolClient;
    }

    public IProtocolService getProtocolService() {
        return protocolService;
    }

    public void setProtocolService(IProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
