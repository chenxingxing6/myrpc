package com.mydubbo.proxy;

import com.mydubbo.rpc.RPCRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:39
 * Desc: 处理请求
 */
public class ProcessorHandler implements Runnable {
    private Socket socket;
    private Object service;

    public ProcessorHandler() {

    }

    public ProcessorHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    public void run() {
        System.out.println("开始处理请求....");
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            // 反序列化
            RPCRequest rpcRequest = (RPCRequest) inputStream.readObject();
            Object result = invoke(rpcRequest);
            // 响应请求
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Object invoke(RPCRequest rpcRequest){
        Object[] args = rpcRequest.getParameters();
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), types);
            return method.invoke(service, args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
