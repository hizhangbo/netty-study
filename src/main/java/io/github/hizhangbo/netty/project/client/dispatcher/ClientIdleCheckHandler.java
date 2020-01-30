package io.github.hizhangbo.netty.project.client.dispatcher;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Bob
 * @date 2020-01-30 22:59
 */
public class ClientIdleCheckHandler extends IdleStateHandler {
    public ClientIdleCheckHandler() {
        super(0, 5, 0);
    }


}
