package com.xh.qychat.infrastructure.integration.qychat.properties;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 会话存档
 *
 * @author H.Yang
 * @date 2023/6/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "qychat.chatdata")
public class ChatDataProperties {

    /**
     * 企业微信ID
     */
    private String corpid;

    /**
     * 会话存档中的 secret
     */

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

    /**
     * 接收消息事件
     */
    private Receive receive;


    @Data
    public class Receive {
        private String token;
        private String encodingAesKey;
    }
}
