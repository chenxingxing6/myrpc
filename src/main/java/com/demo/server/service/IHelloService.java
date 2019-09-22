package com.demo.server.service;

import com.demo.server.entity.User;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:05
 * Desc:
 */
public interface IHelloService {
    public String sayHello(String msg);

    public String saveUser(User user);
}
