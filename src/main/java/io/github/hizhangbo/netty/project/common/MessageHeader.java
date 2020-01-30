package io.github.hizhangbo.netty.project.common;

import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-25 13:06
 */
@Data
public class MessageHeader {
    private int version = 1;
    private int opCode;
    private long streamId;
}
