package io.github.hizhangbo.netty.protobuf.exp2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author Bob
 * @date 2020-01-03 13:38
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message.Parent> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message.Parent message) throws Exception {
        Message.Parent.ChildType childType = message.getChildType();

        if (childType == Message.Parent.ChildType.Child1) {
            System.out.println(
                    String.format("{id:%d,name:%s,email:%s}",
                            message.getChild1().getId(),
                            message.getChild1().getName(),
                            message.getChild1().getEmail()));
        } else if (childType == Message.Parent.ChildType.Child2) {
            System.out.println(
                    String.format("{age:%d,name:%s}",
                            message.getChild2().getAge(),
                            message.getChild2().getName()));
        } else {
            System.out.println("类型不正确！");
        }


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
