package io.github.hizhangbo.netty.project.common;

/**
 * @author Bob
 * @date 2020-01-25 13:07
 */
public class ResponseMessage extends Message<OperationResult> {
    @Override
    public Class getMessageBodyDecodeClass(int opCode) {
        return OperationType.fromOpCode(opCode).getOperationResultClazz();
    }
}
