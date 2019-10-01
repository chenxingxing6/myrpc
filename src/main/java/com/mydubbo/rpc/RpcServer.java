package com.mydubbo.rpc;

import com.alibaba.fastjson.JSON;
import com.mydubbo.config.ProtocolConfig;
import com.mydubbo.config.ProtocolEnum;
import com.mydubbo.config.Rpc;
import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.registry.RegistryDiscoveryFactory;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolServer;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: lanxinghua
 * Date: 2019/9/30 15:48
 * Desc:
 */
public class RpcServer {
    // 配置文件
    private ProtocolConfig config;
    // 服务注册发现
    private AbstractRegistryDiscovery registryDiscovery;
    // 有注解的Class
    private static List<String> className = new ArrayList<>();
    // 自动注册服务
    private boolean autoRegistry = true;

    public RpcServer(){}

    public RpcServer(ProtocolConfig config, RegistryDiscoveryFactory registryDiscoveryFactory) {
        this.config = config;
        this.registryDiscovery = registryDiscoveryFactory.getRegistryDiscovery();
    }

    /**
     * 启动服务
     */
    public void start(){
        String configName = config.name;
        String host = config.host;
        Integer port = config.port;
        String charset = config.charset;
        ProtocolEnum protocolEnum = ProtocolEnum.getInstance(configName);
        if (protocolEnum == null){
            throw new RuntimeException("传输协议配置有误");
        }
        // 如果服务没有，就自动注册服务
        if (autoRegistry || registryDiscovery.countServer() == 0){
            autoRegistryService();
        }
        IProtocolServer service = protocolEnum.getProtocolService();
        System.out.println("[服务注册] size:" + registryDiscovery.countServer());
        registryDiscovery.listServer().entrySet().forEach(r -> {
            System.out.println(String.format("%s - %s", r.getKey(), JSON.toJSONString(r.getValue())));
        });
        service.start(new URL(host, port), charset, registryDiscovery);
    }

    /**
     * 服务注册
     * @param interfaceName
     * @param implClass
     */
    public void register(String interfaceName, Class implClass){
        registryDiscovery.register(new URL(config.host, config.port), interfaceName, implClass);
    }

    /**
     * 通过注解方式注册服务
     */
    public void autoRegistryService(){
        try {
            scanClass(config.scanPackage);
            if (className.isEmpty()){
                return;
            }
            for (String c : className) {
                Class cls = Class.forName(c);
                if (cls == null){
                    continue;
                }
                if (cls.isAnnotationPresent(Rpc.class)){
                    String name = cls.getInterfaces()[0].getTypeName();
                    register(name, cls);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void scanClass(String scanPath){
        java.net.URL url = RpcServer.class.getResource("/"+scanPath.replaceAll("\\.", "/"));
        File file = new File(url.getFile());
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                scanClass(scanPath + "." + f.getName());
            } else {
                className.add(scanPath.replaceAll("/", ".") + "." + f.getName().replace(".class", ""));
            }
        }
    }
}
