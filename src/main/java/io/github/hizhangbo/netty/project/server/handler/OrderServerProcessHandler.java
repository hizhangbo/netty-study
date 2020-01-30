package io.github.hizhangbo.netty.project.server.handler;

import io.github.hizhangbo.netty.project.common.Operation;
import io.github.hizhangbo.netty.project.common.OperationResult;
import io.github.hizhangbo.netty.project.common.RequestMessage;
import io.github.hizhangbo.netty.project.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

/**
 * @author Bob
 * @date 2020-01-26 13:43
 */
@Log4j2
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        if (ctx.channel().isActive() && ctx.channel().isWritable()) {
            ctx.writeAndFlush(responseMessage);
        } else {
            log.error("message dropped.ResponseMessage:{}", responseMessage);
        }

    }
}
