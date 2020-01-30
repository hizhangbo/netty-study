package io.github.hizhangbo.netty.project.common;

import io.github.hizhangbo.netty.project.common.auth.AuthOperation;
import io.github.hizhangbo.netty.project.common.auth.AuthOperationResult;
import io.github.hizhangbo.netty.project.common.keepalive.KeepaliveOperation;
import io.github.hizhangbo.netty.project.common.keepalive.KeepaliveOperationResult;
import io.github.hizhangbo.netty.project.common.order.OrderOperation;
import io.github.hizhangbo.netty.project.common.order.OrderOperationResult;

/**
 * @author Bob
 * @date 2020-01-25 13:07
 */
public enum OperationType {

    AUTH(1, AuthOperation.class, AuthOperationResult.class),
    KEEPALIVE(2, KeepaliveOperation.class, KeepaliveOperationResult.class),
    ORDER(3, OrderOperation.class, OrderOperationResult.class);

    private int opCode;
    private Class<? extends Operation> operationClazz;
    private Class<? extends OperationResult> operationResultClazz;

    OperationType(int opCode, Class<? extends Operation> operationClazz, Class<? extends OperationResult> operationResultClazz) {
        this.opCode = opCode;
        this.operationClazz = operationClazz;
        this.operationResultClazz = operationResultClazz;
    }

    public static OperationType fromOpCode(int opCode) {
        OperationType result = null;
        for (OperationType operationType : OperationType.values()) {
            if (opCode == operationType.opCode) {
                result = operationType;
            }
        }
        return result;
    }

    public static OperationType fromOperation(Operation operation) {
        OperationType result;

        if (operation instanceof AuthOperation) {
            result = AUTH;
        } else if (operation instanceof KeepaliveOperation) {
            result = KEEPALIVE;
        } else if (operation instanceof OrderOperation) {
            result = ORDER;
        } else {
            result = null;
        }
        return result;
    }

    public int getOpCode() {
        return opCode;
    }

    public Class<? extends Operation> getOperationClazz() {
        return operationClazz;
    }

    public Class<? extends OperationResult> getOperationResultClazz() {
        return operationResultClazz;
    }
}
