package io.github.hizhangbo.netty.project.common;

import io.github.hizhangbo.netty.project.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-25 13:05
 */
@Data
public abstract class Message<T extends MessageBody> {
    private MessageHeader messageHeader;
    private T messageBody;

    public T getMessageBody() {
        return messageBody;
    }

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(messageHeader.getVersion());
        byteBuf.writeLong(messageHeader.getStreamId());
        byteBuf.writeInt(messageHeader.getOpCode());
        byteBuf.writeBytes(JsonUtil.toJson(messageBody).getBytes());
    }

    public abstract Class<T> getMessageBodyDecodeClass(int opCode);

    public void decode(ByteBuf msg) {
        int version = msg.readInt();
        long streamId = msg.readLong();
        int opCode = msg.readInt();

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setVersion(version);
        messageHeader.setOpCode(opCode);
        messageHeader.setStreamId(streamId);
        this.messageHeader = messageHeader;

        Class<T> bodyClazz = getMessageBodyDecodeClass(opCode);
        this.messageBody = JsonUtil.fromJson(msg.toString(CharsetUtil.UTF_8), bodyClazz);
    }
}
