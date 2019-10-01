package com.mydubbo.registry.localfile;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * User: lanxinghua
 * Date: 2019/9/30 16:13
 * Desc: 本地文件存储,只需要重写save和get方法
 */
public class FileRegisterDiscovery extends AbstractRegistryDiscovery {
    // 注册文件路径
    private static final String registerFilePath = "img/localregister.txt";

    @Override
    public void save() {
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

    @Override
    public Map<String, Map<URL, Class>> get() {
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
