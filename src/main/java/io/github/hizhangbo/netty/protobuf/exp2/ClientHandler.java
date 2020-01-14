package io.github.hizhangbo.netty.protobuf.exp2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author Bob
 * @date 2020-01-03 13:38
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        Message.Parent parent = null;

        if (0 == random) {
            parent = Message.Parent.newBuilder()
                    .setChildType(Message.Parent.ChildType.Child1)
                    .setChild1(Message.Child1.newBuilder().setId(1).setName("child1").setEmail("123@qq.com").build())
                    .build();
        } else {
            parent = Message.Parent.newBuilder()
                    .setChildType(Message.Parent.ChildType.Child2)
                    .setChild2(Message.Child2.newBuilder().setName("child2").setAge(18).build())
                    .build();

        }
        ctx.writeAndFlush(parent);
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
