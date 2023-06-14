package com.xh.qychat.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jedis 配置
 *
 * <p>@ConfigurationProperties和@Configuration区别：<p/>
 * <p>
 * - @ConfigurationProperties 将yaml或者xml中的配置文件放到类中
 * - @Configuration或@Component 都是使 Spring @ComponentScan 能够扫到该类，把当前类放到 Spring 容器中，可以做为Bean使用
 *
 * <p>@Configuration和@Component区别</p>
 * - @Component 注解可以当做配置类，但是并不会为其生成CGLIB代理Class；当使用 @Configuration注解时，生成当前对象的子类Class
 * <p>
 * 参考地址：
 * https:blog.csdn.net/long476964/article/details/80626930
 *
 * @author H.Yang
 * @date 2021/3/24
 */
@Data
@ConfigurationProperties(prefix = "redis.pool")
public class JedisPoolProperties {

    /**
     * 设置最大连接总数
     */
    private Integer maxTotal = 40;
    /**
     * 设置最大空闲数
     */
    private Integer maxIdle = 20;
    /**
     * 设置最小空闲数
     */
    private Integer minIdle = 8;
    /**
     * 设置最大等待时间
     */
    private Integer maxWaitMillis = 5 * 1000;

    /**
     * 在获取连接的时候检查有效性, 默认false
     */
    private Boolean testOnBorrow = false;
    /**
     * 在空闲时检查有效性, 默认false
     */
    private Boolean testOnReturn = false;
    /**
     * 是否启用pool的jmx管理功能, 默认true
     */
    private Boolean jmxEnabled = true;
    /**
     * Idle时进行连接扫描
     */
    private Boolean testWhileIdle = true;
    /**
     * 是否启用后进先出, 默认true
     */
    private Boolean lifo = true;
    /**
     * 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
     */
    private Boolean blockWhenExhausted = true;

}
