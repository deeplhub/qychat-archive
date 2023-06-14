package com.xh.qychat.infrastructure.config;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.infrastructure.properties.JedisPoolProperties;
import com.xh.qychat.infrastructure.properties.JedisProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2021/3/24
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({JedisProperties.class, JedisPoolProperties.class})
public class JedisAutoConfiguration {

    private final JedisProperties jedisProperties;
    private final JedisPoolProperties jedisPoolProperties;

    private GenericObjectPoolConfig getJedisPoolConfig() {
        log.debug("JedisPool Connection pool initialization...");
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        // 设置最大连接总数
        poolConfig.setMaxTotal(jedisPoolProperties.getMaxTotal());
        // 设置最大空闲数
        poolConfig.setMaxIdle(jedisPoolProperties.getMaxIdle());
        // 设置最小空闲数
        poolConfig.setMinIdle(jedisPoolProperties.getMinIdle());
        // 设置最大等待时间
        poolConfig.setMaxWaitMillis(jedisPoolProperties.getMaxWaitMillis());
        // 在获取连接的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(jedisPoolProperties.getTestOnBorrow());
        // 在空闲时检查有效性, 默认false
        poolConfig.setTestOnReturn(jedisPoolProperties.getTestOnReturn());
        // 是否启用pool的jmx管理功能, 默认true
        poolConfig.setJmxEnabled(jedisPoolProperties.getJmxEnabled());
        // Idle时进行连接扫描
        poolConfig.setTestWhileIdle(jedisPoolProperties.getTestWhileIdle());
        // 是否启用后进先出, 默认true
        poolConfig.setLifo(jedisPoolProperties.getLifo());
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        // poolConfig.setTimeBetweenEvictionRunsMillis(-1);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        // poolConfig.setNumTestsPerEvictionRun(10);
        // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        // poolConfig.setMinEvictableIdleTimeMillis(60000);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(jedisPoolProperties.getBlockWhenExhausted());
        // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
        // poolConfig.setSoftMinEvictableIdleTimeMillis(1800000);

        poolConfig.setJmxEnabled(false);

        return poolConfig;
    }

    /**
     * 单节点
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "redis.model", havingValue = "standalone", matchIfMissing = true)
    public JedisPool jedisPool() {
        log.debug("JedisPool standalone connection initialization...");
        if (StrUtil.isNotBlank(jedisProperties.getPassword())) {
            return new JedisPool(this.getJedisPoolConfig(), //
                    jedisProperties.getHost(), //
                    jedisProperties.getPort(), //
                    jedisProperties.getTimeout(), // 响应超时（默认2000ms）
                    jedisProperties.getPassword(), //
                    jedisProperties.getDatabase()
            );
        }

        return new JedisPool(this.getJedisPoolConfig(), //
                jedisProperties.getHost(), //
                jedisProperties.getPort(), //
                jedisProperties.getDatabase()
        );

    }

    /**
     * 集群
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "redis.model", havingValue = "cluster", matchIfMissing = false)
    public JedisCluster jedisCluster() {
        JedisProperties.Cluster cluster = jedisProperties.getCluster();
        // 设置redis集群的节点信息
        Set<HostAndPort> nodes = cluster.getNodes().stream().map(node -> {
            String[] nodeInfo = node.split(":");
            //添加集群节点
            return new HostAndPort(nodeInfo[0], Integer.parseInt(nodeInfo[1]));
        }).collect(Collectors.toSet());


        // 需要密码连接的创建对象方式
        if (StrUtil.isNotBlank(jedisProperties.getPassword())) {
            return new JedisCluster(nodes, jedisProperties.getTimeout(), 2000, cluster.getMaxRedirects(), jedisProperties.getPassword(), this.getJedisPoolConfig());
        }

        return new JedisCluster(nodes, jedisProperties.getTimeout(), cluster.getMaxRedirects(), this.getJedisPoolConfig());
    }

}
