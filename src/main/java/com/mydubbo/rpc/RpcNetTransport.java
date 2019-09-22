package com.mydubbo.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * User: lanxinghua
 * Date: 2019/9/22 16:13
 * Desc:
 */
public class RpcNetTransport {
    private String host;
    private int port;

    public RpcNetTransport() {
    }

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Socket createSocket(){
        System.out.println("创建socket连接......");
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        }catch (Exception e){
            throw new RuntimeException("连接建立失败");
        }
        return socket;
    }

    public Object sendRequest(RPCRequest rpcRequest){
        Socket socket = null;
        try {
            socket = createSocket();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcRequest);
            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object result = inputStream.readObject();
            inputStream.close();
            outputStream.close();
            return result;
        }catch (Exception e){
            throw new RuntimeException("发送数据异常 msg:" + e.getMessage());
        }finally {
            if (socket != null){
                try {
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
