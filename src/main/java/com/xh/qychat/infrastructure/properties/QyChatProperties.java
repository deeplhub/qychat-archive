package com.xh.qychat.infrastructure.properties;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qychat.chatdata")
public class QyChatProperties {

    /**
     * 企业微信ID
     */
    private String corpid;

    private String secret;

    /**
     * 私钥
     */
    private String privateKey = ResourceUtil.readUtf8Str("private.key");

    /**
     * 一次拉取的消息条数，最大值1000条，超过1000条会返回错误
     */
    private Long limit = 1000L;

    /**
     * 超时时间，单位秒
     */
    private Long timeout = 10L;
}
