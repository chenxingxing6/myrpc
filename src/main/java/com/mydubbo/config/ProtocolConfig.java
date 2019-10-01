package com.mydubbo.config;

/**
 * User: lanxinghua
 * Date: 2019/9/30 14:56
 * Desc: 协议配置
 */
public class ProtocolConfig {
    // 服务协议名
    public static String name = "http";

    // 服务IP地址
    public static String host = "localhost";

    // 服务端口
    public static Integer port = 8088;

    // 字符集
    public static String charset = "UTF-8";

    // 扫描包路径
    public static String scanPackage = "com.demo";

    public ProtocolConfig(String name, String host, Integer port, String charset, String scanPackage) {
        ProtocolConfig.name = name;
        ProtocolConfig.host = host;
        ProtocolConfig.port = port;
        ProtocolConfig.charset = charset;
        ProtocolConfig.scanPackage = scanPackage;
    }
}
