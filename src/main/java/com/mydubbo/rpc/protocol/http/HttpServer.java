package com.mydubbo.rpc.protocol.http;

import com.mydubbo.rpc.framework.URL;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:59
 * Desc:
 */
public class HttpServer {
    public void start(URL url){
        // 实例一个Tomcat
        Tomcat tomcat = new Tomcat();

        // 构建Server
        Server server = tomcat.getServer();

        // 获取Service
        Service service = server.findService("Tomcat");

        // 构建Connector
        Connector connector = new Connector();
        connector.setPort(url.getPort());
        connector.setURIEncoding("UTF-8");

        // 构建引擎
        Engine engine = new StandardEngine();
        engine.setDefaultHost(url.getHostName());

        // 构建Host
        Host host = new StandardHost();
        host.setName(url.getHostName());

        // 构建Context
        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        // 按照server.xml进行配置
        host.addChild(context);
        engine.addChild(host);
        service.setContainer(engine);
        server.addService(service);

        // tomcat是一个servlet,设置路径与映射
        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/client/*", "dispatcher");

        // 启动服务，接受请求
        try {
            tomcat.start();
            tomcat.getServer().await();
            System.out.println("服务启动成功.....");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
