package com.example.wechartapplogintool.common.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommonUtil {
    static Map<String, Object> mutexCache = new ConcurrentHashMap<>();

    /**
     * 获取UUID，防止高并发生成的uuid相同
     */
    public static String getUuid(String prefix) {
        Object mutex4key = mutexCache.computeIfAbsent(prefix, k -> new Object());
        String uuid;
        synchronized (mutex4key) {
            uuid = (UUID.randomUUID().toString()).replace("-", "").toUpperCase(Locale.ROOT);
        }
        return prefix + uuid;
    }
}
