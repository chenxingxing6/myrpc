package com.mydubbo.registry.localfile;

import com.mydubbo.registry.IRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:13
 * Desc: 本地文件存储
 */
public class FileRegisterDiscovery implements IRegistryDiscovery {
    private static Map<String/*接口名*/, Map<URL, Class>> REGISTER = new HashMap<String, Map<URL, Class>>();
    // 注册文件路径
    private static final String registerFilePath = "file/localregister.txt";

    @Override
    public void register(URL url, String interfaceName, Class implClass) {
        Map<URL, Class> map = new HashMap<URL, Class>();
        map.put(url, implClass);
        REGISTER.put(interfaceName, map);
        saveFile();
    }

    @Override
    public Class discovery(URL url, String interfaceName) {
        REGISTER = getFile();
        return Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.get(url)).orElseThrow(() -> new RuntimeException("service not found!"));
    }

    @Override
    public URL randomServer(String interfaceName){
        REGISTER = getFile();
        Set<URL> urls = Optional.ofNullable(REGISTER.get(interfaceName)).map(r -> r.keySet()).orElseThrow(() -> new RuntimeException("service not found!"));
        if (urls == null || urls.isEmpty()){
            throw new RuntimeException("service not found!");
        }
        // 这里就返回第一个
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
