package com.jiangj;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器，channel注册后，会执行里面相应的初始化方法
 * @author Jiang Jian
 * @since 2019/12/29
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 通过SocketChannel获取对应的管道
        ChannelPipeline pipeline = ch.pipeline();

        // 通过管道，添加handler
        // HttpServerCodec由netty提供的助手类，可以理解为拦截器
        // 当请求到服务器，我们需要做解码，响应到客户端需要做编码
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        //  添加自定义助手类，返回'hello netty'
        pipeline.addLast("customHandler", new CustomHandler());
    }
}
