package com.mydubbo.rpc.protocol.http;

import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolServer;
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
public class HttpServer implements IProtocolServer {
    private static final String TOMCAT = "Tomcat";

    @Override
    public void start(URL url, String charset, IRegistryDiscovery registryDiscovery){
        // 实例一个Tomcat
        Tomcat tomcat = new Tomcat();

        // 构建Server
        Server server = tomcat.getServer();

        // 获取Service
        Service service = server.findService(TOMCAT);

        // 构建Connector
        Connector connector = new Connector();
        connector.setPort(url.getPort());
        connector.setURIEncoding(charset);

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
        service.addConnector(connector);

        // tomcat是一个servlet,设置路径与映射
        String servletName = "dispatcher";
        tomcat.addServlet(contextPath, servletName, new DispatcherServlet(url, registryDiscovery));
        context.addServletMappingDecoded("/client/*", servletName);

        // 启动服务，接受请求
        try {
            System.out.println("Tomcat..服务启动成功.....");
            tomcat.start();
            tomcat.getServer().await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
