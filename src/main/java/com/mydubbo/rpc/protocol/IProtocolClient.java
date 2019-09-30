package com.mydubbo.rpc.protocol;

import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;

/**
 * User: lanxinghua
 * Date: 2019/9/30 17:24
 * Desc:
 */
public interface IProtocolClient {
    /**
     * 客户端发送请求
     * @param url
     * @param invocation
     * @return
     */
    public String send(URL url, Invocation invocation);
}
