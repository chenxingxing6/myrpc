package com.demo.provider;

import com.demo.provider.api.IHelloService;
import com.demo.provider.impl.HelloService;
import com.mydubbo.rpc.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:55
 * Desc: 服务提供
 */
public class ProviderStart {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");
        // 服务注册
        rpcServer.register(IHelloService.class.getName(), HelloService.class);
        // 启动服务
        rpcServer.start();
        System.out.println(rpcServer);
    }
}
