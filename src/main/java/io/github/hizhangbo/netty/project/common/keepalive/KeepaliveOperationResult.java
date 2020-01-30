package io.github.hizhangbo.netty.project.common.keepalive;

import io.github.hizhangbo.netty.project.common.OperationResult;
import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-25 16:50
 */
@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;
}
