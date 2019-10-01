package com.demo.consumer;

import com.demo.provider.api.IHelloService;
import com.demo.provider.api.ILogService;
import com.demo.provider.entity.User;
import com.mydubbo.rpc.RpcClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:56
 * Desc:
 */
public class ConsumerStart {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcClient rpcClient = (RpcClient) context.getBean("rpcClient");
        IHelloService helloService = rpcClient.getProxy(IHelloService.class);
        //System.out.println(helloService.sayHello("lanxinghua"));
        //System.out.println(helloService.getUser());
        //System.out.println(helloService.saveUser(new User()));

        // 注解方式
        ILogService logService = rpcClient.getProxy(ILogService.class);
        logService.log("lanxinghua");
    }
}
