package io.github.hizhangbo.netty.project.server;

import io.github.hizhangbo.netty.project.server.codec.OrderFrameDecoder;
import io.github.hizhangbo.netty.project.server.codec.OrderFrameEncoder;
import io.github.hizhangbo.netty.project.server.codec.OrderProtocolDecoder;
import io.github.hizhangbo.netty.project.server.codec.OrderProtocolEncoder;
import io.github.hizhangbo.netty.project.server.handler.MetricHandler;
import io.github.hizhangbo.netty.project.server.handler.OrderServerProcessHandler;
import io.github.hizhangbo.netty.project.server.handler.ServerIdleCheckHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutionException;

/**
 * @author Bob
 * @date 2020-01-26 13:58
 */
@Log4j2
public class Server {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        EventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));

        serverBootstrap.group(bossGroup, workerGroup);

        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        MetricHandler metricHandler = new MetricHandler();
        GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(new NioEventLoopGroup(), 100 * 1024 * 1024, 100 * 1024 * 1024);
        EventExecutorGroup businessEventExecutor = new UnorderedThreadPoolEventExecutor(10, new DefaultThreadFactory("business"));

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("TShandler", globalTrafficShapingHandler);
                pipeline.addLast("IdleCheck", new ServerIdleCheckHandler());
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast("MetricHandler", metricHandler);
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast("FlushEnhance", new FlushConsolidationHandler(5, true));
                pipeline.addLast(businessEventExecutor, new OrderServerProcessHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();
        log.info("server started.");
        channelFuture.channel().closeFuture().get();
    }
}
