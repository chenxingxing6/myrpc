package com.demo.provider.impl;

import com.demo.provider.api.ILogService;
import com.mydubbo.config.Rpc;

/**
 * @Author: cxx
 * @Date: 2019/10/1 20:03
 * 通过注解方式发布dubbo服务
 */
@Rpc
public class LogService implements ILogService {
    @Override
    public void log(String msg) {
        System.out.println("【日志】" + msg);
    }
}
