## 手写RPC(Remote Procedure Call)
要实现一个RPC不算难，难的是实现一个高性能高可靠的RPC框架。  
##### 实现功能
> 1.支持多种协议传输socket,http,dubbo   
> 2.支持多种注册方式file,redis,zookeepre   
> 3.可以动态切换协议和注册方式，只需要更改配置文件  
> 4.Netty实现 （更新2019.10.1）   
> 5.注解方式发布服务@Rpc  （更新2019.10.1）

---
### 一、前言
RPC要解决的两个问题   
> 1.解决分布式系统中，服务之间的调用问题。     
2.远程调用时，要能够像本地调用一样方便，让调用者感知不到远程调用的逻辑。

完整RPC调用过程：   
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/1.jpg)    

> 以左边的Client端为例，Application就是rpc的调用方，Client Stub就是我们上面说到的代理对象，其实内部是通过rpc方
式来进行远程调用的代理对象，至于Client Run-time Library，则是实现远程调用的工具包，比如jdk的Socket，最后通过底
层网络实现实现数据的传输。

> 这个过程中最重要的就是序列化和反序列化了，因为数据传输的数据包必须是二进制的，你直接丢一个Java对象过去，人家可不
认识，你必须把Java对象序列化为二进制格式，传给Server端，Server端接收到之后，再反序列化为Java对象。

---
### 二、项目结构
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/3.png)    

---
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/8.png)    

---
如果要补充协议，只需要实现IProtocolClient,IProtocolService接口
```java
package com.mydubbo.rpc.protocol;

import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 17:24
 * Desc:
 */
public interface IProtocolClient {
    /**
     * 客户端发送请求
     * @param url
     * @param invocation
     * @return
     */
    public Object send(URL url, Invocation invocation);
}
```

---
```java
package com.mydubbo.rpc.protocol;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 17:24
 * Desc:
 */
public interface IProtocolServer {
    /**
     * 启动服务
     * @param url
     * @param charset
     * @param registryDiscovery
     */
    public void start(URL url, String charset, AbstractRegistryDiscovery registryDiscovery);
}
```

---
如果要实现多种注册中心，只需继承AbstractRegistryDiscovery
```java
package com.mydubbo.registry;

import com.mydubbo.rpc.framework.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:09
 * Desc: 服务注册，发现接口
 */
public abstract class AbstractRegistryDiscovery {
    protected static Map<String/*接口名*/, Map<URL, Class>> REGISTER = new HashMap<String, Map<URL, Class>>();

    /**
     * 服务注册
     * @param url
     * @param interfaceName
     * @param implClass
     */
    public void register(URL url, String interfaceName, Class implClass){
        Map<URL, Class> map = new HashMap<URL, Class>();
        map.put(url, implClass);
        REGISTER.put(interfaceName, map);
        save();
    }

    /**
     * 服务发现
     * @param url
     * @param interfaceName
     * @return
     */
    public Class discovery(final URL url, String interfaceName){
        REGISTER = get();
        return Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.get(url)).orElseThrow(() -> new RuntimeException("service not found!"));
    }

    /**
     * 负载均衡，获取可用服务地址
     * @param interfaceName
     * @return
     */
    public URL randomServer(String interfaceName){
        REGISTER = get();
        Set<URL> urls = Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.keySet()).orElseThrow(() -> new RuntimeException("service not found!"));
        if (urls == null || urls.isEmpty()){
            throw new RuntimeException("service not found!");
        }
        // 这里就返回第一个
        return urls.iterator().next();
    }

    /**
     * 保存到注册中心
     */
    public abstract void save();

    /**
     * 从注册中心获取
     */
    public abstract Map<String, Map<URL, Class>> get();
}
```
---

### 三、内置tomca

```xml
<Server port="8005" shutdown="SHUTDOWN">
  <Service name="Catalina">
    <Connector port="8080" protocol="HTTP/1.1"
       connectionTimeout="20000"
       redirectPort="8443" 
       URIEncoding="UTF-8"/>
    <Engine name="Catalina" defaultHost="localhost">
       <Host name="localhost"  appBase="webapps"
         unpackWARs="true" autoDeploy="true">
         <Context path="" doBase="WORKDIR" reloadable="true"/>
       </Host>
    </Engine>
  </Service>
</Server>
```
---
```java
package com.mydubbo.rpc.protocol.http;

import com.mydubbo.registry.AbstractRegistryDiscovery;
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
    public void start(URL url, String charset, AbstractRegistryDiscovery registryDiscovery){
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
```
---

### 四、注册中心
> 1.本地文件  
2.zk  
3.redis  
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/5.png)    

---
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/4.png)    

---
### 五、测试
```html
## 协议配置信息
## 支持socket,http,dubbo
config.protocol.name=socket
config.protocol.host=localhost
config.protocol.port=8080
config.protocol.charset=UTF-8

## 服务注册 支持localfile,redis,zookeeper
service.registry.type=redis
#service.registry.type=localfile
#service.registry.type=zookeeper
```

---
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:spring.properties"/>

    <!-- 协议配置信息 -->
    <bean id="protocolConfig" class="com.mydubbo.config.ProtocolConfig">
        <constructor-arg name="name" value="${config.protocol.name}"/>
        <constructor-arg name="host" value="${config.protocol.host}"/>
        <constructor-arg name="port" value="${config.protocol.port}"/>
        <constructor-arg name="charset" value="${config.protocol.charset}"/>
    </bean>

    <!-- 服务注册方式 -->
    <bean id="registryDiscoveryFactory" class="com.mydubbo.registry.RegistryDiscoveryFactory">
        <constructor-arg name="registryType" value="${service.registry.type}"/>
    </bean>

    <bean id="rpcClient" class="com.mydubbo.rpc.RpcClient">
        <constructor-arg name="config" ref="protocolConfig"/>
        <constructor-arg name="registryDiscoveryFactory" ref="registryDiscoveryFactory"/>
    </bean>

    <bean id="rpcServer" class="com.mydubbo.rpc.RpcServer">
        <constructor-arg name="config" ref="protocolConfig"/>
        <constructor-arg name="registryDiscoveryFactory" ref="registryDiscoveryFactory"/>
    </bean>
</beans>
```
---

![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/6.png)    


```java
package com.demo.consumer;

import com.demo.provider.api.IHelloService;
import com.mydubbo.rpc.RpcClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:56
 * Desc: 服务启动
 */
public class ConsumerStart {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcClient rpcClient = (RpcClient) context.getBean("rpcClient");
        IHelloService helloService = rpcClient.getProxy(IHelloService.class);
        System.out.println(helloService.sayHello("lanxinghua"));
        System.out.println(helloService.getUser());
        System.out.println(helloService.saveUser(helloService.getUser()));
    }
}

```

---
![desc](https://raw.githubusercontent.com/chenxingxing6/myrpc/master/img/7.png)    
```java
package com.demo.provider;

import com.demo.provider.api.IHelloService;
import com.demo.provider.impl.HelloService;
import com.mydubbo.rpc.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:55
 * Desc: 服务提供
 */
public class ProviderStart {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");
        // 服务注册
        rpcServer.register(IHelloService.class.getName(), HelloService.class);
        // 启动服务
        rpcServer.start();
    }
}
```
---
