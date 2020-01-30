package io.github.hizhangbo.netty.project.common.keepalive;

import io.github.hizhangbo.netty.project.common.Operation;
import io.github.hizhangbo.netty.project.common.OperationResult;

/**
 * @author Bob
 * @date 2020-01-25 16:50
 */
public class KeepaliveOperation extends Operation {

    private long time;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public OperationResult execute() {
        return new KeepaliveOperationResult(time);
    }
}
