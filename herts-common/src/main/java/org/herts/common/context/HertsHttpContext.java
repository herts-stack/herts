package org.herts.common.context;

import java.util.Map;

public class HertsHttpContext {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void set(Map<String, Object> data) {
        threadLocal.set(data);
    }

    public static Map<String, Object> get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
