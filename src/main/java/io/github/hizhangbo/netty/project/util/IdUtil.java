package io.github.hizhangbo.netty.project.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Bob
 * @date 2020-01-25 21:12
 */
public final class IdUtil {
    private static final AtomicLong IDX = new AtomicLong();

    private IdUtil() {
    }

    public static long nextId() {
        return IDX.incrementAndGet();
    }
}
