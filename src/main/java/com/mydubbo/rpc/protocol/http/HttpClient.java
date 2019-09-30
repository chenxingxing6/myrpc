package com.mydubbo.rpc.protocol.http;

import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:39
 * Desc:
 */
public class HttpClient {
    public String post(URL url, Invocation invocation){
        try {
            java.net.URL u = new java.net.URL("http", url.getHostName(), url.getPort(), "/client/");
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 输出流
            OutputStream out = connection.getOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(invocation);
            outputStream.flush();
            outputStream.close();

            // 输入流
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
