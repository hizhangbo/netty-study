package io.github.hizhangbo.netty.tcp.exp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Bob
 * @date 2020-01-03 13:38
 */
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        String message = new String(content, CharsetUtil.UTF_8);
        System.out.println("服务端收到消息：" + message);

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(content);
        messageProtocol.setLen(content.length);

        ctx.writeAndFlush(messageProtocol);
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelReadComplete 消息接收完毕");
//        MessageProtocol messageProtocol = new MessageProtocol();
//        byte[] content = "服务端消息回复".getBytes(CharsetUtil.UTF_8);
//        messageProtocol.setContent(content);
//        messageProtocol.setLen(content.length);
//
//        ctx.writeAndFlush(messageProtocol);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
