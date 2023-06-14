package com.xh.qychat.infrastructure.redis;

import java.util.Map;

/**
 * Title:
 * Description:
 *
 * @author H.Yang
 * @date 2021/3/16
 */
public interface CacheService {

    Map<String, Object> cacheMap();

}
