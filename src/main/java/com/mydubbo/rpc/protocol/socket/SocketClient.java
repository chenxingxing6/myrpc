package com.mydubbo.rpc.protocol.socket;

import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:22
 * Desc:
 */
public class SocketClient implements IProtocolClient {
    @Override
    public Object send(URL url, Invocation invocation) {
        Socket socket = null;
        try {
            socket = new Socket(url.getHostName(), url.getPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(invocation);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            return ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (socket != null){
                try {
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
