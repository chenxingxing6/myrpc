package com.mydubbo.config;

import com.mydubbo.rpc.protocol.IProtocolClient;
import com.mydubbo.rpc.protocol.IProtocolServer;
import com.mydubbo.rpc.protocol.dubbo.DubboClient;
import com.mydubbo.rpc.protocol.dubbo.DubboServer;
import com.mydubbo.rpc.protocol.http.HttpClient;
import com.mydubbo.rpc.protocol.http.HttpServer;
import com.mydubbo.rpc.protocol.socket.SocketClient;
import com.mydubbo.rpc.protocol.socket.SocketServer;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:23
 * Desc: 服务调用协议
 */
public enum ProtocolEnum {
    SOCKET("socket", new SocketClient(), new SocketServer()),
    HTTP("http", new HttpClient(), new HttpServer()),
    DUBBO("dubbo", new DubboClient(), new DubboServer());

    ProtocolEnum( String key, IProtocolClient protocolClient, IProtocolServer protocolService) {
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
    private IProtocolServer protocolService;
    private String key;

    public IProtocolClient getProtocolClient() {
        return protocolClient;
    }

    public void setProtocolClient(IProtocolClient protocolClient) {
        this.protocolClient = protocolClient;
    }

    public IProtocolServer getProtocolService() {
        return protocolService;
    }

    public void setProtocolService(IProtocolServer protocolService) {
        this.protocolService = protocolService;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
