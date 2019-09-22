package com.demo.server;

import com.demo.server.service.IHelloService;
import com.demo.server.service.impl.HelloService;
import com.mydubbo.proxy.RpcServerProxy;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:54
 * Desc:
 */
public class ServerAppMain {
    public static void main(String[] args) {
        // 新建服务
        IHelloService helloService = new HelloService();
        // 创建代理
        RpcServerProxy proxy = new RpcServerProxy();
        // 发布服务
        proxy.publish(helloService, 8080);
    }
}
