package com.jiangj.websocket;

import com.jiangj.netty.HelloServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Jiang Jian
 * @since 2019/12/29
 */
public class WSServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();

        try {
            // netty服务器创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 1、设置主从线程组，2、设置nio双向通道，3、子处理器，用于处理workerGroup
            serverBootstrap.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            channelFuture.channel().closeFuture().sync();
        }finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
