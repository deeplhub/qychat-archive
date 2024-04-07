package com.xh.qychat.infrastructure.util;

import cn.hutool.core.util.StrUtil;

/**
 * Title:
 * Description:
 *
 * @author H.Yang
 * @date 2021/3/25
 */
public class KeyUtils {

    public static String get(Class<?> clazz, Object columns) {
        String builder = clazz.getSimpleName() +
                ":" +
                columns + "";
        return builder;
    }

    public static String get(String keyName, Object columns) {
        String builder = keyName +
                ":" +
                columns + "";
        return builder;
    }

    public static String get(String keyName, Object... columns) {
        StringBuilder builder = new StringBuilder();
        builder.append(keyName);
        builder.append(":");

        for (Object obj : columns) {
            builder.append(obj + "");
            builder.append(":");
        }

        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String get(Class<?> clazz, String keyName, Object columns) {
        if (StrUtil.isBlank(keyName)) {
            throw new RuntimeException("参数不合法");
        }
        String builder = clazz.getSimpleName() +
                "_" +
                keyName +
                ":" +
                columns + "";
        return builder;
    }


    public static String get(Object... keyNames) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : keyNames) {
            builder.append(obj + "");
            builder.append(":");
        }
        if (builder.length() > 0) {
            return builder.deleteCharAt(builder.length() - 1).toString();
        }
        return null;
    }

}
