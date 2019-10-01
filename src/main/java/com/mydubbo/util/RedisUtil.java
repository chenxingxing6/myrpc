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
        return jedis.set(key.getBytes(), IoUtil.toByteArray(value));
    }

    public static String setString(String key, String value){
        return set(key, value);
    }

    public static String getString(String key){
        return jedis.get(key);
    }

    public static Object getObject(String key){
        return IoUtil.toObject(jedis.get(key.getBytes()));
    }

    private static String set(String key, String value){
        return set(key, value, expire);
    }

    private static String set(String key, String value, long second){
        // NX XX
        // EX PX
       return jedis.set(key, value, "NX", "EX", second);
    }

    public static void main(String[] args) {
        /*Jedis jedis = new Jedis(host, port);
        System.out.println(jedis.ping());
        System.out.println(host);*/
        System.out.println(setString("key2", "hello world"));
        System.out.println(getString("key2"));
    }
}
