package io.github.hizhangbo.netty.rpc.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Bob
 * @date 2020-01-14 22:30
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    private RemoteService remoteService;

    public RemoteService getRemoteService() {
        return this.remoteService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        remoteService = new RemoteService();
        remoteService.setContext(ctx);
        System.out.println("RemoteService completed.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        synchronized (RpcClient.LOCK) {
            String result = msg.toString();
            System.out.println("远程调用结果：" + result);
            // 唤醒代理对象执行远程调用时的线程
            remoteService.setResult(result);
            RpcClient.LOCK.notify();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
