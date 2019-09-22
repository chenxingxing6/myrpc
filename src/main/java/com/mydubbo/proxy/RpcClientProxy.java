package com.mydubbo.proxy;

import java.lang.reflect.Proxy;

/**
 * User: lanxinghua
 * Date: 2019/9/22 16:05
 * Desc:
 */
public class RpcClientProxy {

    public <T> T clientProxy(Class<T> interfaceCls, String host, int port){
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class<?>[]{interfaceCls}, new RemoteInvocationHandler(host, port));
    }

}
