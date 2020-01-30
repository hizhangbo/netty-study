package io.github.hizhangbo.netty.project.common.auth;

import io.github.hizhangbo.netty.project.common.OperationResult;
import lombok.Data;

/**
 * @author Bob
 * @date 2020-01-25 16:49
 */
@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;
}
