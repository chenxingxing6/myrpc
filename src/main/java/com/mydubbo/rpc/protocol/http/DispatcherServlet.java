package com.mydubbo.rpc.protocol.http;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: lanxinghua
 * Date: 2019/9/30 12:09
 * Desc:
 */
public class DispatcherServlet extends HttpServlet {
    private URL url;
    private AbstractRegistryDiscovery registryDiscovery;

    public DispatcherServlet(URL url, AbstractRegistryDiscovery registryDiscovery) {
        this.url = url;
        this.registryDiscovery = registryDiscovery;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       new HttpServletHandler(url, registryDiscovery).handler(req, resp);
    }
}
