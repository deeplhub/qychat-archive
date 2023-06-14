package com.xh.qychat.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/3/29
 */
@Data
@ConfigurationProperties(prefix = "redis")
public class JedisProperties {

    private String model = "standalone";
    private String host = "127.0.0.1";
    private String password;
    private Integer port = 6379;
    private Integer database = 0;

    /**
     * 响应超时,单位：毫秒
     */
    private Integer timeout = 3 * 1000;

    private Cluster cluster = new Cluster();
    private Sentinel sentinel = new Sentinel();


    @Data
    public class Cluster {
        private Set<String> nodes;
        /**
         * 最大重定向次数
         */
        private Integer maxRedirects = 5;
    }

    @Data
    public class Sentinel {
        private Set<String> nodes;
        /**
         * 主节点名称
         */
        private String master;
        private String password;
    }
}
