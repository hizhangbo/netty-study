package io.github.hizhangbo.netty.project.common.auth;

import io.github.hizhangbo.netty.project.common.Operation;
import io.github.hizhangbo.netty.project.common.OperationResult;
import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-25 16:48
 */
@Data
public class AuthOperation extends Operation {

    private final String userName;
    private final String password;

    @Override
    public OperationResult execute() {
        if ("admin".equalsIgnoreCase(this.userName)) {
            return new AuthOperationResult(true);
        }
        return new AuthOperationResult(false);
    }
}
