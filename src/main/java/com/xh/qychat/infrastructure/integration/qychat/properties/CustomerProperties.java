package com.xh.qychat.infrastructure.integration.qychat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客户联系
 *
 * @author H.Yang
 * @date 2023/6/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "qychat.customer")
public class CustomerProperties {

    /**
     * 企业微信ID
     */
    private String corpid;

    /**
     * 客户联系中的 secret
     */

    private String secret;
}
