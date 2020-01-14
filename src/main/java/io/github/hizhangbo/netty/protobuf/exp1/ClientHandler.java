package io.github.hizhangbo.netty.protobuf.exp1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author Bob
 * @date 2020-01-03 13:38
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessagePOJO.Message.Builder test = MessagePOJO.Message.newBuilder().setId(1).setName("test");
        ctx.writeAndFlush(test);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(ctx.channel().remoteAddress() + " server: " + buf.toString(UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
