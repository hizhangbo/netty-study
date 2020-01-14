package io.github.hizhangbo.netty.tcp.exp2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Bob
 * @date 2020-01-09 12:40
 */
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
