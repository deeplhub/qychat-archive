package com.xh.qychat.infrastructure.redis.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.xh.qychat.infrastructure.redis.JedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.params.SetParams;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 单节点模式
 *
 * @author H.Yang
 * @date 2023/3/30
 */
@Slf4j
@AllArgsConstructor
@Configuration
@ConditionalOnProperty(value = "redis.model", havingValue = "standalone", matchIfMissing = true)
public class JedisPoolRepository implements JedisRepository {

    private final JedisPool jedisPool;

    /**
     * 处理JedisException，写入日志并返回连接是否中断。
     *
     * @param jedisException
     */
    public void handleJedisException(Exception jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            log.error("Redis connection lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().contains("READONLY"))) {
                log.error("Redis connection  are read-only slave.", jedisException);
            }
        } else {
            log.error("Redis 异常", jedisException);
        }

        throw new RuntimeException("Redis异常");
    }

    private Jedis getJedis() {

        return jedisPool.getResource();
    }

    /************************************************************************
     *
     *  KEY 操作
     *
     *************************************************************************/

    /**
     * 清除所有库的KEY
     *
     * @return
     */
    public String flushAll() {
        try (Jedis jedis = this.getJedis()) {
            return jedis.flushAll();
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 清除当前选择数据库中的所有key
     *
     * @return
     */
    public String flushDB() {
        try (Jedis jedis = this.getJedis()) {
            return jedis.flushDB();
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return null;
    }

    /**
     * 将当前数据库中的key转移到有dbindex索引的数据库
     *
     * @param key
     * @param dbIndex
     * @return
     */
    public long move(String key, int dbIndex) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.move(key, dbIndex);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 批量删除
     *
     * @param keyList 要删除的key的集合
     * @return
     */
    public boolean mdel(List<String> keyList) {
        try (Jedis jedis = this.getJedis()) {
            //获取pipeline
            Pipeline pipeline = jedis.pipelined();
            for (String key : keyList) {
                pipeline.del(key);
            }
            //执行结果同步，这样才能保证结果的正确性。实际上不执行该方法也执行了上面的命令，但是结果确不一定完全正确。
            //注意
            pipeline.sync();

            return true;
        } catch (Exception e) {
            this.handleJedisException(e);
        }

        return false;
    }


    @Override
    public long isExists(String... keys) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.exists(keys);
        } catch (Exception e) {
            this.handleJedisException(e);
        }

        return 0L;
    }


    @Override
    public boolean isExist(String key) {
        try (Jedis jedis = this.getJedis()) {
            // 如果数据存在则返回0，不存在返回1
            return jedis.setnx(key, key) == 0;
        } catch (Exception e) {
            this.handleJedisException(e);
        }

        return false;
    }


    @Override
    public boolean isExist(String key, String value) {
        try (Jedis jedis = this.getJedis()) {
            // 如果数据存在则返回0，不存在返回1
            return jedis.setnx(key, value) == 0;
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return false;
    }

    /**
     * @param key         锁名称
     * @param flagId      锁标识（锁值），用于标识锁的归属
     * @param secondsTime 最大超时时间
     * @return
     */
    @Deprecated
    public boolean lock(String key, String flagId, Integer secondsTime) {
        SetParams params = new SetParams();
        params.ex(secondsTime);
        params.nx();

        // TODO 该方法未做验证，当使用的时候自行验证
        String res = this.getJedis().set(key, flagId, params);
        log.info(res);
        if (StrUtil.isNotBlank(res) && res.equals("OK")) {
            return true;
        }

        return false;
    }

    //    /**
//     * 释放分布式锁
//     *
//     * @param lockKey 锁的 key
//     * @param flagId  锁归属标识
//     * @return 是否释放成功
//     */
//    @Deprecated
//    public boolean unLock(String lockKey, String flagId) {
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        // TODO 该方法未做验证，当使用的时候自行验证
//        Object result = this.getJedis().eval(script, Collections.singletonList(lockKey), Collections.singletonList(flagId));
//        log.info(result.toString());
//        if ("1L" == result) { // 判断执行结果
//            return true;
//        }
//        return false;
//    }
//
    @Override
    public long del(String... keys) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.del(keys);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    @Override
    public String getType(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.type(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    @Override
    public Set<String> getKeys(String pattern) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.keys(pattern);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new HashSet<>();
    }

    @Override
    public long getDbSize() {
        try (Jedis jedis = this.getJedis()) {
            return jedis.dbSize();
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 设置超时
     *
     * @param key
     * @param seconds 超时时间（单位为秒）
     * @return
     */
    @Override
    public long expire(String key, int seconds) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 设置超时 - 时间戳(秒)
     *
     * @param key
     * @param unixTime 时间戳(秒)
     * @return
     */
    @Override
    public long expireAt(String key, int unixTime) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.expireAt(key, unixTime);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 获取key的过期时间
     * <p>
     * 如果key存在过期时间，返回剩余生存时间(秒)；
     * 如果key是永久的，返回-1；
     * 如果key不存在或者已过期，返回-2。
     *
     * @param key
     * @return
     */
    @Override
    public long getKeyExpire(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 移除key的过期时间，将其转换为永久状态。
     * <p>
     * 如果返回1，代表转换成功。
     * 如果返回0，代表key不存在或者之前就已经是永久状态。
     *
     * @param key
     * @return
     */
    @Override
    public long persist(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.persist(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 为键 key 储存的数字值加上一
     * <p>
     * 如果键 key 不存在， 那么它的值会先被初始化为 0 ， 然后再执行 INCR 命令。
     *
     * @param key
     * @return
     */
    @Override
    public long incr(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.incr(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 为键 key 储存的数字值加上一
     * <p>
     * 如果键 key 不存在， 那么它的值会先被初始化为 0 ， 然后再执行 INCR 命令。
     * <p>
     * 自增,默认为0。返回自增操作后的值
     *
     * @param key
     * @param seconds
     * @return
     */
    @Override
    public long incr(String key, int seconds) {
        try (Jedis jedis = this.getJedis()) {
            long incr = jedis.incr(key);
            if (incr > 0) {
                jedis.expire(key, seconds);
            }
            return incr;
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    @Override
    public long incrLock(String key, int seconds) {
        long incr = 0;
        try (Jedis jedis = this.getJedis()) {
            synchronized (key) {
                incr = jedis.incr(key);
                if (incr > 0) {
                    jedis.expire(key, seconds);
                }
            }
            return incr;

        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return incr;
    }


    /**
     * value 加increment
     *
     * @param key       key
     * @param increment 加几
     * @return
     */
    @Override
    public long incrby(String key, int increment) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.incrBy(key, increment);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * value 加increment
     *
     * @param key       key
     * @param increment 加几
     * @param seconds
     * @return
     */
    @Override
    public long incrby(String key, int increment, int seconds) {
        try (Jedis jedis = this.getJedis()) {
            long incr = jedis.incrBy(key, increment);
            jedis.expire(key, seconds);
            return incr;
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * value 减1
     * <p>
     * 必须是字符型数字
     *
     * @param key
     * @return
     */
    @Override
    public long decr(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.decr(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * value 减increment
     *
     * @param key
     * @param increment
     * @return
     */
    @Override
    public long decrby(String key, int increment) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.decrBy(key, increment);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /****************************************************************************************************
     *
     *
     * String操作
     *
     *
     *
     ****************************************************************************************************/

    /**
     * 返回key的value的长度
     *
     * @param key
     * @return
     */
    @Override
    public long strlen(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.strlen(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 设置某个key的value
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public String set(String key, String value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.set(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 字符串后追加内容
     *
     * @param key           key
     * @param appendContent 要追加的内容
     * @return
     */
    @Override
    public long append(String key, String appendContent) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.append(key, appendContent);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 获取某个key的value，类型要对，只能value是string的才能获取
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.get(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 给名称为key的string赋予上一次的value
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String getSet(String key, String value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.getSet(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 给名称为key的string赋予上一次的value
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    @Override
    public String getSet(String key, String value, int seconds) {
        try (Jedis jedis = this.getJedis()) {
            String set = jedis.getSet(key, value);
            if (StrUtil.isNotBlank(set)) {
                jedis.expire(key, seconds);
            }
            return set;
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 返回库中多个string（它们的名称为key1，key2…）的value
     *
     * @param keys
     * @return
     */
    @Override
    public List<String> mget(String... keys) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.mget(keys);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new ArrayList<>();
    }

    /**
     * 如果不存在名称为key的string，则向库中添加string，名称为key，值为value
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long setnx(String key, String value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.setnx(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 向库中添加string（名称为key，值为value）同时，设定过期时间time
     *
     * @param key
     * @param value
     * @param seconds 必须只正数
     * @return
     */
    @Override
    public String setex(String key, String value, int seconds) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 同时给多个string赋值，名称为key i的string赋值value i
     * <p>Description: key1, value1, key2, value2,…key N, value N</p>
     *
     * @param keysvalues
     * @return
     */
    @Override
    public String mset(String... keysvalues) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.mset(keysvalues);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 如果所有名称为key i的string都不存在，则向库中添加string，名称key i赋值为value i
     * <p>Description: msetnx(key1, value1, key2, value2,…key N, value N)：</p>
     *
     * @param keysvalues
     * @return
     */
    @Override
    public long msetnx(String... keysvalues) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.msetnx(keysvalues);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }


    /****************************************************************************************************
     *
     *
     * List操作
     *
     *
     *
     ****************************************************************************************************/

    /**
     * 在名称为key的list尾添加一个值为value的元素
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long rpush(String key, String... value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.rpush(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 在名称为key的list头添加一个值为value的 元素
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long lpush(String key, String... value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 获取key所有元素
     *
     * @param key
     * @return
     */
    @Override
    public List<String> lrange(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new ArrayList<>();
    }

    /**
     * 返回名称为key的list中index位置的元素
     *
     * @param key
     * @param indexSubscript
     * @return
     */
    @Override
    public String lindex(String key, long indexSubscript) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.lindex(key, indexSubscript);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 给名称为key的list中index位置的元素赋值为value
     *
     * @param key
     * @param value
     * @param indexSubscript
     * @return
     */
    @Override
    public String lset(String key, String value, long indexSubscript) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.lset(key, indexSubscript, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 对列表进行剪裁，保留指定闭区间的元素(索引位置也会重排)
     *
     * @param key        列表key
     * @param startIndex 开始索引位置
     * @param endIndex   结束索引位置
     * @return
     */
    @Override
    public String ltrim(String key, Integer startIndex, Integer endIndex) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.ltrim(key, startIndex, endIndex);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 返回名称为key的list的长度
     *
     * @param key
     * @return
     */
    @Override
    public long llen(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.llen(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 返回并删除名称为key的list中的首元素
     *
     * @param key
     * @return
     */
    @Override
    public String lpop(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.lpop(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 返回并删除名称为key的list中的尾元素
     *
     * @param key
     * @return
     */
    @Override
    public String rpop(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.rpop(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /****************************************************************************************************
     *
     *
     * Hash操作
     *
     *
     ****************************************************************************************************/

    /**
     * 添加Map类型的值
     *
     * @param key
     * @param hash
     * @return
     */
    @Override
    public long hset(String key, Map<String, String> hash) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hset(key, hash);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }


    /**
     * 向名称为key的hash中添加元素field i<—>value i
     * <p>Description: hmset(key, field1, value1,…,field N, value N)</p>
     *
     * @param key
     * @param hash
     * @return
     */
    @Override
    public String hmset(String key, Map<String, String> hash) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hmset(key, hash);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }


    /**
     * 向名称为key的hash中添加元素field<—>value
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public long hset(String key, String field, String value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }


    /**
     * 返回名称为key的hash中field对应的value
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public String hget(String key, String field) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return "";
    }

    /**
     * 返回名称为key的hash中field i对应的value
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public List<String> hmget(String key, String... fields) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new ArrayList<>();
    }

    /**
     * 名称为key的hash中是否存在键为field的域
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public boolean hexists(String key, String field) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hexists(key, field);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return false;
    }


    /**
     * 删除名称为key的hash中键为field的域
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public long hdel(String key, String... fields) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hdel(key, fields);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 给某个hash表中的某个field的value增加多少
     * <p>
     * 对某个field累加指定的数值，返回累加后的值(如果希望减，可以给个 负数的参数)
     *
     * @param key       hash表的key
     * @param field     表中的某个field
     * @param increment 增加多少
     * @return
     */
    @Override
    public long hincrBy(String key, String field, long increment) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hincrBy(key, field, increment);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 给某个hash表中的某个field的value增加多少
     * <p>
     * 对某个field累加指定的数值，返回累加后的值(如果希望减，可以给个 负数的参数)
     *
     * @param key       hash表的key
     * @param field     表中的某个field
     * @param increment 增加多少
     * @param seconds   秒
     * @return
     */
    @Override
    public long hincrBy(String key, String field, long increment, int seconds) {
        try (Jedis jedis = this.getJedis()) {
            long hincr = jedis.hincrBy(key, field, increment);
            if (hincr > 0) {
                jedis.expire(key, seconds);
            }
            return hincr;
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }


    /**
     * 返回名称为key的hash中元素个数
     *
     * @param key
     * @return
     */
    @Override
    public long hlen(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hlen(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }


    /**
     * 返回名称为key的hash中所有键
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> hkeys(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hkeys(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new HashSet<>();
    }


    /**
     * 返回名称为key的hash中所有键对应的value
     *
     * @param key
     * @return
     */
    @Override
    public List<String> hvals(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hvals(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new ArrayList<>();
    }

    /**
     * 返回名称为key的hash中所有的键（field）及其对应的value
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new HashMap<>();
    }


    /****************************************************************************************************************************************************************
     *
     *
     * SET
     *
     * 在Redis 中，可以将 Set 类型看作为没有排序的字符集合，Set 集合中不允许出现重复的元素
     *
     *
     ****************************************************************************************************************************************************************/

    /**
     * 添加Set类型的值
     * <p>
     * 向 set 中添加数据，如果该 key 的值已有则不会重复添加
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long sadd(String key, String... value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 根据KEY获得 set 中所有的成员
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return new HashSet<>();
    }


    /**
     * 删除 set 中指定的成员
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long srem(String key, String... value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 将key1中的元素key1Member移动到key2中
     *
     * @param key1       来源集合key
     * @param key2       目的地集合key
     * @param key1Member key1中的元素
     * @return 1成功，0失败
     */
    @Override
    public long smove(String key1, String key2, String key1Member) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.smove(key1, key2, key1Member);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 判断参数中指定的成员是否在该 set 中，1表示存在，0表示不存在或key本身不存在
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean sismember(String key, String value) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return false;
    }

    /**
     * 获取 set 中成员的数量
     *
     * @param key
     * @return
     */
    @Override
    public long scard(String key) {
        try (Jedis jedis = this.getJedis()) {
            return jedis.scard(key);
        } catch (Exception e) {
            this.handleJedisException(e);
        }
        return 0L;
    }

    /**
     * 获取当前时间距离第二天凌晨的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    @Override
    public long getSecondsNextEarlyMorning() {

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    @Override
    public String getGenerateNo() {
        //格式化日期
        String prefix = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        return prefix + String.format("%1$06d", this.incrLock(prefix, 70));
    }

    @Override
    public String getGenerateNo(String prefix) {
        //格式化日期
        return prefix + String.format("%1$06d", this.incrLock(prefix, 70));
    }
}
