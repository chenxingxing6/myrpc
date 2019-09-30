package com.demo.provider;

import com.demo.provider.api.IHelloService;
import com.demo.provider.impl.HelloService;
import com.mydubbo.registry.Register;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.http.HttpServer;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:55
 * Desc: 服务提供
 */
public class ProviderStart {
    public static void main(String[] args) {
        // 服务注册
        URL url = new URL("localhost", 8888);
        Register.registServer(url, IHelloService.class.getName(), HelloService.class);

        // 启动服务
        HttpServer server = new HttpServer();
        server.start(url);
    }
}
