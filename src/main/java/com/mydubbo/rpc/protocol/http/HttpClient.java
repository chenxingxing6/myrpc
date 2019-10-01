package com.mydubbo.rpc.protocol.http;

import com.mydubbo.config.ProtocolConfig;
import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolClient;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:39
 * Desc:
 */
public class HttpClient implements IProtocolClient {

    @Override
    public Object send(URL url, Invocation invocation) {
        try {
            java.net.URL u = new java.net.URL("http", url.getHostName(), url.getPort(), "/client/");
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 输出流
            OutputStream out = connection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            // 输入流
            InputStream is = connection.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Object result = ois.readObject();
            ois.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
