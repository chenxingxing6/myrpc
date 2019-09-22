package com.demo.client;

import com.demo.server.service.IHelloService;
import com.mydubbo.proxy.RpcClientProxy;

/**
 * User: lanxinghua
 * Date: 2019/9/22 16:22
 * Desc:
 */
public class ClientAppMain {
    public static void main(String[] args) {
        RpcClientProxy clientProxy = new RpcClientProxy();
        IHelloService helloService = clientProxy.clientProxy(IHelloService.class, "localhost",8080);
        String result = helloService.sayHello("777");
        System.out.println(result);
    }
}
