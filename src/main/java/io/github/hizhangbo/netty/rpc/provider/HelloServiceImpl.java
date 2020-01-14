package io.github.hizhangbo.netty.rpc.provider;

import io.github.hizhangbo.netty.rpc.protocol.HelloService;

/**
 * @author Bob
 * @date 2020-01-14 10:19
 */
public class HelloServiceImpl implements HelloService {
    private int count = 0;

    @Override
    public String hello(String msg) {
        return "远程调用结果返回：" + msg + " 第" + (++count) + "次";
    }
}
