package com.mydubbo.rpc.protocol.socket;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:23
 * Desc:
 */
public class SocketServer implements IProtocolServer {
    private Socket socket;
    @Override
    public void start(URL url, String charset, AbstractRegistryDiscovery registryDiscovery) {
        System.out.println("Socket..服务启动成功.....");
        try {
            ServerSocket serverSocket = new ServerSocket(url.getPort());
            while (true){
                socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Invocation invocation = (Invocation) ois.readObject();

                // 寻找实现类，通过反射执行
                Class implClass = registryDiscovery.discovery(new URL(url.getHostName(), url.getPort()), invocation.getInterfaceName());
                Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());

                // 执行结果返回
                Object result = method.invoke(implClass.newInstance(), invocation.getParams());
                System.out.println("结果：" + result.toString());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(result);
                oos.flush();
                oos.close();
                ois.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
