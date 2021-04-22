package com.regent.rpush.common;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工具类
 *
 * @author 钟宝林
 * @since 2021/2/25/025 20:33
 **/
public class SingletonUtil {

    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonUtil() {
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static <T> T get(String key, Factory<T> factory) {
        return get(key, factory, false);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static <T> T get(String key, Factory<T> factory, boolean forceRefresh) {
        Type objectClass = factory.getClass().getGenericInterfaces()[0];
        key += objectClass.getTypeName();
        T obj = (T) OBJECT_MAP.get(key);
        if (obj == null || forceRefresh) {
            synchronized (key.intern()) {
                if (obj == null || forceRefresh) {
                    obj = factory.newInstance();
                    OBJECT_MAP.put(key, obj);
                }
            }
        }
        return obj;
    }

    public static <T> T get(Factory<T> factory) {
        return get(null, factory);
    }

    public interface Factory<T> {
        T newInstance();
    }


}
