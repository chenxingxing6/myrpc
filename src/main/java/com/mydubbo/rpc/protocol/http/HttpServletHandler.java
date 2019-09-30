package com.mydubbo.rpc.protocol.http;

import com.mydubbo.registry.Register;
import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

/**
 * User: lanxinghua
 * Date: 2019/9/30 12:12
 * Desc:
 */
@SuppressWarnings("unchecked")
public class HttpServletHandler {
    public void handler(HttpServletRequest request, HttpServletResponse response){
        try {
            // http请求转为流对象
            InputStream is = request.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Invocation invocation = (Invocation) ois.readObject();

            // 寻找实现类，通过反射执行
            Class implClass = Register.findServer(new URL("localhost", 8888), invocation.getInterfaceName());
            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());

            // 执行结果返回
            Object result = method.invoke(implClass.newInstance(), invocation.getParams());
            System.out.println("结果：" + result.toString());
            IOUtils.write(result.toString(), response.getOutputStream(), "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
