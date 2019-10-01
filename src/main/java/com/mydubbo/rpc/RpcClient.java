package com.mydubbo.rpc;

import com.mydubbo.config.ProtocolConfig;
import com.mydubbo.config.ProtocolEnum;
import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.registry.RegistryDiscoveryFactory;
import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:18
 * Desc:
 */
public class RpcClient{
    // 配置文件
    private ProtocolConfig config;
    // 服务注册发现
    private AbstractRegistryDiscovery registryDiscovery;

    public RpcClient(ProtocolConfig config, RegistryDiscoveryFactory registryDiscoveryFactory) {
        this.config = config;
        this.registryDiscovery = registryDiscoveryFactory.getRegistryDiscovery();
    }

    public <T> T getProxy(Class interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(
                        interfaceClass.getName(),
                        method.getName(),
                        args,
                        method.getParameterTypes()
                );

                // 获取服务调用地址
                URL url = registryDiscovery.randomServer(interfaceClass.getName());

                // 调用服务
                ProtocolEnum protocolEnum = ProtocolEnum.getInstance(config.name);
                if (protocolEnum == null){
                    throw new RuntimeException("传输协议配置错误");
                }
                IProtocolClient client = protocolEnum.getProtocolClient();
                return client.send(url, invocation);
            }
        });
    }
}
