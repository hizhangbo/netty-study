package io.github.hizhangbo.netty.protobuf.exp1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author Bob
 * @date 2020-01-03 13:38
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessagePOJO.Message message = (MessagePOJO.Message) msg;

        SocketAddress address = ctx.channel().remoteAddress();
        System.out.println(String.format("{id:%d,name:%s}", message.getId(), message.getName()));

//        ctx.channel().eventLoop().execute(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端消息读取完毕", StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
