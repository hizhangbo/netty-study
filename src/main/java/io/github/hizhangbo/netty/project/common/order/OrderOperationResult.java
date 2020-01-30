package io.github.hizhangbo.netty.project.common.order;

import io.github.hizhangbo.netty.project.common.OperationResult;
import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-25 16:52
 */
@Data
public class OrderOperationResult extends OperationResult {

    private final int tableId;
    private final String dish;
    private final boolean complete;
}
