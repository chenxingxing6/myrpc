package com.mydubbo.rpc.framework;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return Objects.equals(hostName, url.hostName) &&
                Objects.equals(port, url.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostName, port);
    }

    @Override
    public String toString() {
        return "URL{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                '}';
    }
}
