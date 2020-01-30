package io.github.hizhangbo.netty.project.client.dispatcher;

import io.github.hizhangbo.netty.project.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-26 21:10
 */
@Data
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private final RequestPendingCenter requestPendingCenter;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage responseMessage) throws Exception {
        requestPendingCenter.set(responseMessage.getMessageHeader().getStreamId(), responseMessage.getMessageBody());
    }
}
