package com.demo.consumer;

import com.demo.provider.api.IHelloService;
import com.mydubbo.rpc.framework.ProxyFactory;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:56
 * Desc:
 */
public class ConsumerStart {
    public static void main(String[] args) {
        IHelloService helloService = ProxyFactory.getProxy(IHelloService.class);
        String result = helloService.sayHello("lanxinghua");
        System.out.println(result);
    }
}
