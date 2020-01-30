package io.github.hizhangbo.netty.project.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Bob
 * @date 2020-01-26 10:38
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
