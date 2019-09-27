package com.wen.sakura.util;

import com.wen.sakura.IExecutor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Helper {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> T ignoreCheck(IExecutor<T> executor) {
        try {
            return executor.exe();
        } catch (Throwable throwable) {
            // ignore
        }
        return null;
    }

    public static <T> T uncheck(IExecutor<T> executor) {
        try {
            return executor.exe();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T> T pick(Supplier<T>... ss) {
        for (Supplier<T> s : ss) {
            T v = s.get();
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public static boolean isJDKProxy(Class<?> clazz) {
        return clazz != null && Proxy.isProxyClass(clazz);
    }

    public static boolean isCGLIBProxy(Class<?> clazz) {
        return clazz != null && (null != clazz.getName() && clazz.getName().contains("$$"));
    }

}
