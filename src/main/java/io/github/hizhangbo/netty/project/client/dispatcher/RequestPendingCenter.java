package io.github.hizhangbo.netty.project.client.dispatcher;

import io.github.hizhangbo.netty.project.common.OperationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bob
 * @date 2020-01-26 21:05
 */
public class RequestPendingCenter {

    private Map<Long, OperationResultFuture> map = new ConcurrentHashMap<>();

    public void add(long streamId, OperationResultFuture future) {
        this.map.put(streamId, future);
    }

    public void set(long streamId, OperationResult operationResult) {
        OperationResultFuture operationResultFuture = this.map.get(streamId);
        if (operationResultFuture != null) {
            operationResultFuture.setSuccess(operationResult);
            this.map.remove(streamId);
        }
    }
}
