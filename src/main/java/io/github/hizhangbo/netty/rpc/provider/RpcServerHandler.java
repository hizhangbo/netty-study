package io.github.hizhangbo.netty.rpc.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Bob
 * @date 2020-01-14 10:43
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String rpcMsg = msg.toString();
        System.out.println("客户端消息：" + rpcMsg);

        if (rpcMsg.startsWith("HelloService#hello#")) {
            String result = new HelloServiceImpl()
                    .hello(rpcMsg.substring(rpcMsg.lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
