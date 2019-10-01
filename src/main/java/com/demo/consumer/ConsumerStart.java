package com.demo.consumer;

import com.demo.provider.api.IHelloService;
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
        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 1; i++) {
//            String result = helloService.sayHello("lanxinghua");
//            System.out.println("[" + i + "]" + result);
//        }
//        System.out.println((System.currentTimeMillis() - startTime)/1000);

        System.out.println(helloService.sayHello("lanxinghua"));
        System.out.println(helloService.getUser());
        System.out.println(helloService.saveUser(helloService.getUser()));
    }
}
