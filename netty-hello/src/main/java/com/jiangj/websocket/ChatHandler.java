package com.jiangj.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author Jiang Jian
 * @since 2019/12/29
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，是用于为websocket专门处理文本的对象，frame是消息载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录和管理客户端所有的channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("获取到的消息：" + content);

        /*clients.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame("[服务器在]"
                        + LocalDateTime.now() + "接收到的消息为：" + content)));*/

        // 下面这个方法，和上面for循环效果一致
        clients.writeAndFlush(new TextWebSocketFrame("[服务器在]"
                + LocalDateTime.now() + "接收到的消息为：" + content));
    }

    /**
     * 当客户端打开链接之后
     * 获取客户端的channel并且放到channelGroup中进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    /**
     * 当触发handlerRemoved，ChannelGroup会自动移除客户端的channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id：" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id：" + ctx.channel().id().asShortText());
    }
}
