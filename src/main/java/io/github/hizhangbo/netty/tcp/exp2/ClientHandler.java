package io.github.hizhangbo.netty.tcp.exp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author Bob
 * @date 2020-01-03 13:38
 */
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String msg = "hello:" + i;
            byte[] content = msg.getBytes(UTF_8);
            int length = content.length;

            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);

            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        String content = new String(msg.getContent(), UTF_8);
        System.out.println("客户端接收到的信息：" + content + "，消息长度" + msg.getLen());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
