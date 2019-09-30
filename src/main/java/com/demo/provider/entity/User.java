package com.demo.provider.entity;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:06
 * Desc:
 */
public class User implements Serializable {
    // 类的指纹
    private static final long serialVersionUID = 7123331669516594765L;

    private String userName;

    private int age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
