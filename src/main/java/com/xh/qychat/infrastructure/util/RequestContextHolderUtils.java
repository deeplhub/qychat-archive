package com.xh.qychat.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title: 应用级对象获取工具类
 * Description:
 *
 * @author H.Yang
 * @date 2021/6/24
 */
@Slf4j
public class RequestContextHolderUtils {

    private final ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    public static Map<String, Object> getMapHolder() {
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> headerNames = RequestContextHolderUtils.getRequest().getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            // 排除Cookie字段
            if ("Cookie".equalsIgnoreCase(key)) {
                continue;
            }
            String value = RequestContextHolderUtils.getRequest().getHeader(key);
            map.put(key, value);
        }

        return map;
    }


    /**
     * 获取目标主机的ip
     *
     * @return
     */
    private static String getRemoteHost() {
        String ip = RequestContextHolderUtils.getRequest().getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = RequestContextHolderUtils.getRequest().getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = RequestContextHolderUtils.getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = RequestContextHolderUtils.getRequest().getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
