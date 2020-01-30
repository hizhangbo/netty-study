package io.github.hizhangbo.netty.project.client.dispatcher;

import io.github.hizhangbo.netty.project.common.RequestMessage;
import io.github.hizhangbo.netty.project.common.keepalive.KeepaliveOperation;
import io.github.hizhangbo.netty.project.util.IdUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;

/**
 * @author Bob
 * @date 2020-01-30 23:00
 */
@Log4j2
@ChannelHandler.Sharable
public class KeepaliveHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            log.info("write idle happen.so need to send keepalive to keep connection not closed by server.");
            KeepaliveOperation keepaliveOperation = new KeepaliveOperation();
            RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), keepaliveOperation);
            ctx.writeAndFlush(requestMessage);
        }
        super.userEventTriggered(ctx, evt);
    }
}
