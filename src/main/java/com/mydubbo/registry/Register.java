package com.mydubbo.registry;

import com.mydubbo.rpc.framework.URL;

import java.io.*;
import java.util.*;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:19
 * Desc: 注册中心
 */
public class Register {
    private static Map<String/*接口名*/, Map<URL, Class>> REGISTER = new HashMap<String, Map<URL, Class>>();

    /**
     * 注册文件路径
     */
    private static final String registerFilePath = "file/localregister.txt";

    /**
     * 服务注册
     * @param url
     * @param interfaceName
     * @param implClass
     */
    public static void registServer(URL url, String interfaceName, Class implClass){
        Map<URL, Class> map = new HashMap<URL, Class>();
        map.put(url, implClass);
        System.out.println("服务注册 " + String.format("%s  %s", interfaceName, url.toString()));
        REGISTER.put(interfaceName, map);
        saveFile();
    }

    /**
     * 发现服务
     * @param url  需要重新equals & hashCode方法
     * @param interfaceName
     * @return
     */
    public static Class findServer(final URL url, String interfaceName){
        REGISTER = getFile();
        return Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.get(url)).orElseThrow(() -> new RuntimeException("service not found!"));
    }

    /**
     * 随机获取服务，模拟负载均衡（后面有空实现）
     * @param interfaceName
     * @return
     */
    public static URL randomServer(String interfaceName){
        REGISTER = getFile();
        Set<URL> urls = Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.keySet()).orElse(null);
        if (urls == null || urls.isEmpty()){
            throw new RuntimeException("service not found!");
        }
        return urls.iterator().next();
    }

    /**
     * 注册信息保存到文件中
     */
    private static void saveFile(){
        try {
            File file = new File(registerFilePath);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(REGISTER);
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取文件中注册信息
     * @return
     */
    private static Map<String, Map<URL, Class>> getFile(){
        try {
            File file = new File(registerFilePath);
            if (!file.exists()){
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Map<String, Map<URL, Class>>)ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
