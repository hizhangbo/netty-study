package io.github.hizhangbo.netty.project.common.order;

import com.google.common.util.concurrent.Uninterruptibles;
import io.github.hizhangbo.netty.project.common.Operation;
import io.github.hizhangbo.netty.project.common.OperationResult;
import lombok.extern.java.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Bob
 * @date 2020-01-25 16:52
 */
@Log
public class OrderOperation extends Operation {

    private int tableId;
    private String dish;

    public OrderOperation(int tableId, String dish) {
        this.tableId = tableId;
        this.dish = dish;
    }

    @Override
    public OperationResult execute() {
        log.info("order's executing startup with orderRequest:" + toString());
        // 模拟业务处理
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        log.info("order's executing complete");
        return new OrderOperationResult(tableId, dish, true);
    }
}
