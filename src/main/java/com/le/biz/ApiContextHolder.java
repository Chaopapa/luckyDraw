package com.le.biz;

import com.le.system.entity.SysToken;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 严秋旺
 * @since 2018-11-07 17:16
 **/
public class ApiContextHolder {
    private static final String ATTRIBUTE_TOKEN = "token";
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void setAttributes(SysToken sysToken) {
        Map<String, Object> attr = new HashMap<>();
        attr.put(ATTRIBUTE_TOKEN, sysToken);
        threadLocal.set(attr);
    }

    public static void reset() {
        threadLocal.remove();
    }

    public static SysToken getToken() {
        Map<String, Object> map = threadLocal.get();
        return map == null ? null : (SysToken) map.get(ATTRIBUTE_TOKEN);
    }

}
