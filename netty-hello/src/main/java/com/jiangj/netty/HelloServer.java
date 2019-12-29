package com.jiangj.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现客服端发送一个请求，服务器会返回hello netty
 *
 * @author Jiang Jian
 * @since 2019/12/29
 */
public class HelloServer {

    public static void main(String[] args) throws InterruptedException {

        //定义一对线程组
        // 主线程组，用于接受客户端请求，但不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 从线程组，bossGroup把任务扔给workerGroup，由workerGroup处理任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // netty服务器创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 1、设置主从线程组，2、设置nio双向通道，3、子处理器，用于处理workerGroup
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HelloServerInitializer());

            // 启动server，设置端口8088，启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
