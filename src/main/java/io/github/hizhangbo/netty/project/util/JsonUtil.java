package io.github.hizhangbo.netty.project.util;

import com.google.gson.Gson;
import io.github.hizhangbo.netty.project.common.MessageBody;

/**
 * @author Bob
 * @date 2020-01-25 17:12
 */
public final class JsonUtil {

    private static final Gson GSON = new Gson();

    private JsonUtil() {
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
