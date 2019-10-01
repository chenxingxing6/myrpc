package com.mydubbo.rpc.protocol.dubbo;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.util.IoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * @Author: cxx
 * @Date: 2019/10/1 16:34
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private URL url;
    private AbstractRegistryDiscovery registryDiscovery;

    public NettyServerHandler(URL url, AbstractRegistryDiscovery registryDiscovery) {
        this.url = url;
        this.registryDiscovery = registryDiscovery;
    }

    // 读取客户端的请求数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        Invocation invocation = (Invocation) IoUtil.toObject(req);
        Class implClass = registryDiscovery.discovery(url, invocation.getInterfaceName());
        Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamTypes());
        Object result = method.invoke(implClass.newInstance(), invocation.getParams());
        ctx.writeAndFlush(Unpooled.copiedBuffer(IoUtil.toByteArray(result)));
    }

    // 数据读取完毕事件
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    // 发生异常事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("发生异常:" + cause.getMessage());
        ctx.close();
    }
}
