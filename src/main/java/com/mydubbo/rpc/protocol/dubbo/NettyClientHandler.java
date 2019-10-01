package com.mydubbo.rpc.protocol.dubbo;

import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.util.IoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @Author: cxx
 * @Date: 2019/10/1 16:57
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private Invocation invocation;
    private Object response;

    public NettyClientHandler(Invocation invocation){
        this.invocation = invocation;
    }

    public Object getResponse(){
        return response;
    }

    // 通道就绪，发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer(IoUtil.toByteArray(invocation)));
    }

    // 读取服务端响应数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] resp = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(resp);
        Object result = IoUtil.toObject(resp);
        response = result;
        ctx.close();
    }
}
