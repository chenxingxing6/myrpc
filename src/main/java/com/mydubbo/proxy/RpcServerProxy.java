package com.mydubbo.proxy;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: lanxinghua
 * Date: 2019/9/22 15:33
 * Desc: BIO方式实现
 */
public class RpcServerProxy {
    ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 发布服务
     * @param service
     * @param port
     */
    public void publish(Object service, int port){
        System.out.println("发布服务....." + service.getClass().getSimpleName());
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessorHandler(socket, service));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                try{
                    serverSocket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
