package com.jiangj.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Jiang Jian
 * @since 2019/12/29
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // websocket基于http协议，所以要有http编解码器
        pipeline.addLast(new HttpServerCodec());

        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        // 对httpMessage进行聚合，聚合成FullHttpRequest或者FullHttpResponse
        // 几乎在netty的编程中，都会用到次handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 60));

        // ============以上用于支持http协议================

        /**
         * websocket服务器处理的协议，用于指定给客户端链接访问的路由：/ws
         * 处理握手动作：handshaking（Close, Ping, Pong）
         * 对websocket，都是通过frames进行传输，不通的数据类型对应的frames不通
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义handle
        pipeline.addLast(new ChatHandler());

    }
}
