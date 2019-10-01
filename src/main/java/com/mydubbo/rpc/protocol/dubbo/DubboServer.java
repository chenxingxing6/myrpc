package com.mydubbo.rpc.protocol.dubbo;

import com.mydubbo.registry.AbstractRegistryDiscovery;
import com.mydubbo.rpc.framework.URL;
import com.mydubbo.rpc.protocol.IProtocolServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

/**
 * User: lanxinghua
 * Date: 2019/9/30 18:23
 * Desc:
 */
public class DubboServer implements IProtocolServer {
    @Override
    public void start(URL url, String charset, AbstractRegistryDiscovery registryDiscovery) {
        // 1.创建服务器启动配置助手
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 2.创建一个线程组，接收客户端的连接
        EventLoopGroup boss = new NioEventLoopGroup();
        // 3.创建一个线程池，处理网络
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 2048);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new NettyServerHandler(url, registryDiscovery));
                }
            });
            // 4.绑定端口bind是同步，sync是异步
            ChannelFuture channelFuture = bootstrap.bind(url.getPort());
            System.out.println("netty..服务启动成功.....");
            // 5.关闭通道，关闭线程组
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
