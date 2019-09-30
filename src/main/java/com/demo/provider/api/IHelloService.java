package com.demo.provider.api;

import com.demo.provider.entity.User;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:05
 * Desc:
 */
public interface IHelloService {
    
    public String sayHello(String msg);

    public String saveUser(User user);
}
