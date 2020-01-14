package io.github.hizhangbo.netty.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author Bob
 * @date 2020-01-02 21:39
 */
public class TextWsFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(String.format("%s[客户端]:%s", channel.remoteAddress(), msg.text()));
        channel.writeAndFlush(new TextWebSocketFrame("server time:" + LocalDateTime.now()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端加入]" + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端离开]" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("[向客户端发送消息异常]" + ctx.channel().id().asLongText());
        cause.printStackTrace();
        ctx.close();
    }
}
