package io.github.hizhangbo.netty.rpc.consumer;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Callable;

/**
 * @author Bob
 * @date 2020-01-14 23:50
 */
public class RemoteService implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String parameter;


    /**
     * 代理对象执行远程调用，执行完毕线程阻塞 wait，等待被唤醒
     *
     * @return 远程调用结果
     * @throws Exception 未处理异常
     */
    @Override
    public Object call() throws Exception {
        synchronized (RpcClient.LOCK) {
            System.out.println("RemoteService.call()");
            context.writeAndFlush(parameter);
            // 等待channelRead唤醒
            RpcClient.LOCK.wait();
            return result;
        }
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
