package com.mydubbo.rpc.framework;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/9/30 11:18
 * Desc:
 */
public class URL implements Serializable {
    private String hostName;

    private Integer port;

    public URL() {
    }

    public URL(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
