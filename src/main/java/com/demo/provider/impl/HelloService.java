package com.demo.provider.impl;

import com.demo.provider.entity.User;
import com.demo.provider.api.IHelloService;

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
        System.out.println(user.toString());
        return "保存成功";
    }

    @Override
    public User getUser() {
        User user = new User();
        user.setUserName("lanxinghua");
        user.setAge(22);
        return user;
    }


}
