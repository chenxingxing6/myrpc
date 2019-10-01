package com.mydubbo.rpc.protocol.dubbo;

import com.mydubbo.rpc.framework.Invocation;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.tomcat.util.net.NioChannel;

import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:23
 * Desc:
 */
public class DubboClient implements IProtocolClient {
    @Override
    public Object send(URL url, Invocation invocation) {
        NettyClientHandler handler = new NettyClientHandler(invocation);
        //接受服务端的数据处理类
        try {
            // 1.创建线程组
            EventLoopGroup group = new NioEventLoopGroup();
            // 2.创建启动助手，完善配置
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel s) throws Exception {
                            s.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future = b.connect(url.getHostName(), url.getPort()).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        return handler.getResponse();
    }
}
