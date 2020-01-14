package io.github.hizhangbo.netty.rpc.consumer;

import io.github.hizhangbo.netty.rpc.protocol.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Bob
 * @date 2020-01-14 22:30
 */
public class RpcClient {
    private static ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private RemoteService remoteService;
    private RpcClientHandler rpcClientHandler;
    public static final String LOCK = "locked";
    private int count;

    public static void main(String[] args) throws InterruptedException {
        RpcClient rpcClient = new RpcClient();
        rpcClient.startClient();

        String providerName = "HelloService#hello#";
        HelloService helloService = (HelloService) rpcClient.getBean(HelloService.class, providerName);

        for (; ; ) {
            TimeUnit.SECONDS.sleep(3);
            String remoteResult = helloService.hello("netty rpc example");
            System.out.println(remoteResult);
        }

    }

    /**
     * 获取执行远程调用的代理对象
     *
     * @param serviceClass 远程服务的协议规范 HelloService
     * @param providerName 远程服务的协议名称 HelloService#hello#
     * @return 代理对象
     */
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, ((proxy, method, args) -> {
                    System.out.println("getBean()" + (++count));
                    remoteService = rpcClientHandler.getRemoteService();
                    remoteService.setParameter(providerName + args[0]);
                    return executorService.submit(remoteService).get();
                }));
    }

    private void startClient() {
        rpcClientHandler = new RpcClientHandler();
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(rpcClientHandler);
                    }
                });
        try {
            bootstrap.connect("127.0.0.1", 8080).sync();
            System.out.println("rpc consumer started.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
