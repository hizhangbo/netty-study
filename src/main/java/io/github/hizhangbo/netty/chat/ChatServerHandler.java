package io.github.hizhangbo.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Bob
 * @date 2020-01-01 22:50
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        LocalDateTime now = LocalDateTime.now();
        channels.writeAndFlush(
                String.format("%s[客户端]%s加入聊天\n",
                        now.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                        channel.remoteAddress()));
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        LocalDateTime now = LocalDateTime.now();
        channels.writeAndFlush(
                String.format("%s[客户端]%s离开了，剩余聊天人数%d\n",
                        now.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                        channel.remoteAddress(),
                        channels.size()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("%s上线了", ctx.channel().remoteAddress()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("%s下线了", ctx.channel().remoteAddress()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        LocalDateTime now = LocalDateTime.now();
        channels.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(String.format("%s[客户端]%s:%s\n",
                        now.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                        channel.remoteAddress(),
                        msg));
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
