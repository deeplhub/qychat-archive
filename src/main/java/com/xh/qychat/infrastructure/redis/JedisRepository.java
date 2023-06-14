package com.xh.qychat.infrastructure.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/3/30
 */
public interface JedisRepository {

    /**
     * 判断一个key是否存在
     *
     * @param keys
     * @return
     */
    long isExists(String... keys);

    /**
     * 增加业务幂等性验证
     *
     * @param key
     * @return
     */
    boolean isExist(String key);

    /**
     * 增加业务幂等性验证
     *
     * @param key
     * @param value
     * @return
     */
    boolean isExist(String key, String value);

    /**
     * 删除一个或多个key
     *
     * @param keys
     * @return
     */
    long del(String... keys);


    /**
     * 根据 KEY 获取值类型
     *
     * @param key 键
     * @return
     */
    String getType(String key);

    /**
     * 模糊匹配 KEY 列表
     * <p>
     * 通配符说明：
     * * 0到任意多个字符
     * ? 1个字符
     *
     * @param pattern 通配 KEY
     * @return
     */
    Set<String> getKeys(String pattern);

    /**
     * 返回当前数据库中key的数目
     *
     * @return
     */
    long getDbSize();

    long expire(String key, int seconds);

    long expireAt(String key, int unixTime);

    long getKeyExpire(String key);

    long persist(String key);

    long incr(String key);

    long incr(String key, int seconds);

    /**
     * 为键 key 储存的数字值加上一
     * <p>
     * 安全
     *
     * @param key
     * @param seconds
     * @return
     */
    long incrLock(String key, int seconds);

    long incrby(String key, int increment);

    long incrby(String key, int increment, int seconds);

    long decr(String key);

    long decrby(String key, int increment);

    long strlen(String key);

    String set(String key, String value);

    long append(String key, String appendContent);

    String get(String key);

    String getSet(String key, String value);

    String getSet(String key, String value, int seconds);

    List<String> mget(String... keys);

    long setnx(String key, String value);

    String setex(String key, String value, int seconds);

    String mset(String... keysvalues);

    long msetnx(String... keysvalues);

    long rpush(String key, String... value);

    long lpush(String key, String... value);

    List<String> lrange(String key);

    String lindex(String key, long indexSubscript);

    String lset(String key, String value, long indexSubscript);

    String ltrim(String key, Integer startIndex, Integer endIndex);

    long llen(String key);

    String lpop(String key);

    String rpop(String key);

    long hset(String key, Map<String, String> hash);

    String hmset(String key, Map<String, String> hash);

    long hset(String key, String field, String value);

    String hget(String key, String field);

    List<String> hmget(String key, String... fields);

    boolean hexists(String key, String field);

    long hdel(String key, String... fields);

    long hincrBy(String key, String field, long increment);

    long hincrBy(String key, String field, long increment, int seconds);

    long hlen(String key);

    Set<String> hkeys(String key);

    List<String> hvals(String key);

    Map<String, String> hgetAll(String key);

    long sadd(String key, String... value);

    Set<String> smembers(String key);

    long srem(String key, String... value);

    long smove(String key1, String key2, String key1Member);

    boolean sismember(String key, String value);

    long scard(String key);

    long getSecondsNextEarlyMorning();

    /**
     * 自动生成编号
     * <p>
     * 生成的编号为当前日期（yyyyMMddHHmmss）+6位（从000000开始不足位数补0）
     *
     * @return
     */
    String getGenerateNo();

    /**
     * 自动生成编号
     * <p>
     * 生成的编号为当前日期（yyyyMMddHHmmss）+6位（从000000开始不足位数补0）
     *
     * @param prefix 前缀
     * @return
     */
    String getGenerateNo(String prefix);
}
