package com.mydubbo.util;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * @Author: cxx
 * @Date: 2019/10/1 13:52
 */
public class RedisUtil {
    private static final String host = "localhost";
    private static final int port = 6379;
    private static final long expire = 60*60;
    private static Jedis jedis;
    static {
        jedis = new Jedis(host, port);
        System.out.println("Connection to server sucessfully");
    }

    public static String setObject(String key, Object value){
        return jedis.set(key.getBytes(), toByteArray(value));
    }

    public static String setString(String key, String value){
        return set(key, value);
    }

    public static String getString(String key){
        return jedis.get(key);
    }

    public static Object getObject(String key){
        return toObject(jedis.get(key.getBytes()));
    }

    private static String set(String key, String value){
        return set(key, value, expire);
    }

    private static String set(String key, String value, long second){
        // NX XX
        // EX PX
       return jedis.set(key, value, "NX", "EX", second);
    }

    /**
     * 对象转数组
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public static Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args) {
        /*Jedis jedis = new Jedis(host, port);
        System.out.println(jedis.ping());
        System.out.println(host);*/
        System.out.println(setString("key2", "hello world"));
        System.out.println(getString("key2"));
    }
}
