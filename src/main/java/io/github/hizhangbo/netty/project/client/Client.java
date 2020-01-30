package io.github.hizhangbo.netty.project.client;

import io.github.hizhangbo.netty.project.client.codec.OrderFrameDecoder;
import io.github.hizhangbo.netty.project.client.codec.OrderFrameEncoder;
import io.github.hizhangbo.netty.project.client.codec.OrderProtocolDecoder;
import io.github.hizhangbo.netty.project.client.codec.OrderProtocolEncoder;
import io.github.hizhangbo.netty.project.client.dispatcher.*;
import io.github.hizhangbo.netty.project.common.OperationResult;
import io.github.hizhangbo.netty.project.common.RequestMessage;
import io.github.hizhangbo.netty.project.common.order.OrderOperation;
import io.github.hizhangbo.netty.project.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutionException;

/**
 * @author Bob
 * @date 2020-01-26 13:58
 */
@Log4j2
public class Client {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(new NioEventLoopGroup());

        RequestPendingCenter requestPendingCenter = new RequestPendingCenter();

        KeepaliveHandler keepaliveHandler = new KeepaliveHandler();

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ClientIdleCheckHandler());
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));
                pipeline.addLast(keepaliveHandler);
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
        log.info("client started.");

        long streamId = IdUtil.nextId();

        RequestMessage requestMessage = new RequestMessage(streamId, new OrderOperation(1001, "四喜丸子"));
        OperationResultFuture operationResultFuture = new OperationResultFuture();
        requestPendingCenter.add(streamId, operationResultFuture);

        channelFuture.channel().writeAndFlush(requestMessage);
        OperationResult operationResult = operationResultFuture.get();

        log.info(operationResult);

        channelFuture.channel().writeAndFlush(requestMessage);
        channelFuture.channel().closeFuture().get();
    }
}
