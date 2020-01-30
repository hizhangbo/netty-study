package io.github.hizhangbo.netty.project.client.codec;

import io.github.hizhangbo.netty.project.common.RequestMessage;
import io.github.hizhangbo.netty.project.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author Bob
 * @date 2020-01-26 13:40
 */
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(msg);

        out.add(responseMessage);
    }
}
