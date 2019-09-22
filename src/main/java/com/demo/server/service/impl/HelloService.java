package com.demo.server.service.impl;

import com.demo.server.entity.User;
import com.demo.server.service.IHelloService;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:05
 * Desc:
 */
public class HelloService implements IHelloService{
    public String sayHello(String msg) {
        return "hello msg：" + msg;
    }

    public String saveUser(User user) {
        return "保存成功";
    }
}
