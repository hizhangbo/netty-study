package io.github.hizhangbo.netty.project.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author Bob
 * @date 2020-01-26 10:38
 */
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }
}
