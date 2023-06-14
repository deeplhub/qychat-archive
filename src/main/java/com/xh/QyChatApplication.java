package com.xh;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author H.Yang
 * @date 2021/2/23
 */
@Slf4j
@SpringBootApplication
public class QyChatApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(QyChatApplication.class, args);
        ConfigurableEnvironment configurableEnvironment = ctx.getEnvironment();

        // 加载企业微信SDK
        loadChatSDK(configurableEnvironment.getProperty("qychat.sdk.path"));

        log.info("# ==============================");
        log.info("# It is running ... ");
        log.info("# APPLICATION     : " + configurableEnvironment.getProperty("spring.application.name"));
        log.info("# ACTIVE          : " + configurableEnvironment.getProperty("spring.profiles.active"));
        log.info("# PORT            : " + configurableEnvironment.getProperty("server.port"));
        log.info("# ==============================");
    }

    private static void loadChatSDK(String path) {
        log.info("satrt load qychat sdk... ");

        log.info(path);
        if (StrUtil.isBlank(path)) {
            throw new RuntimeException("未配置企业微信SDK");
        }

        String OS = System.getProperty("os.name").toUpperCase();
        log.info("OS : " + OS);

        try {
            if (OS.contains("WIN")) {
                System.load(path + "/libcrypto-1_1-x64.dll");
                System.load(path + "/libssl-1_1-x64.dll");
                System.load(path + "/libcurl-x64.dll");
                System.load(path + "/WeWorkFinanceSdk.dll");
            } else {
                System.load(path + "/WeWorkFinanceSdk_Java.so");
            }
            log.info("load qychat sdk success.");
        } catch (Exception | Error e) {
            log.error("加载企业微信SDK异常", e);
            throw new RuntimeException("加载企业微信SDK异常", e);
        }

    }

}
