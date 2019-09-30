package com.mydubbo.rpc.framework;

import com.mydubbo.registry.Register;
import com.mydubbo.rpc.protocol.http.HttpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:14
 * Desc:
 */
public class ProxyFactory<T> {
    public static <T> T getProxy(final Class interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(
                        interfaceClass.getName(),
                        method.getName(),
                        args,
                        new Class[]{String.class}
                );

                // 获取服务调用地址
                URL url = Register.randomServer(interfaceClass.getName());

                // 调用服务
                HttpClient client = new HttpClient();
                return client.post(url, invocation);
            }
        });
    }
}
