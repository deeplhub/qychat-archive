package com.xh.qychat.infrastructure.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 签名
 *
 * @author H.Yang
 * @date 2023/7/7
 */
public class SignUtils {

    public static String getSign(Object... args) {
        StringBuffer sb = new StringBuffer();
        for (Object obj : args) {
            sb.append(isEmpty(obj));
        }

        String verify = sb.toString();

        return (StrUtil.isNotBlank(verify)) ? SecureUtil.md5(verify) : "";
    }

    private static String isEmpty(Object obj) {
        return (obj != null) ? obj + "" : "";
    }

}
