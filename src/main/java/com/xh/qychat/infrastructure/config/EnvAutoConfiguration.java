package com.xh.qychat.infrastructure.config;

import com.xh.qychat.infrastructure.constants.EnvConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Title: 环境
 * Description:
 *
 * @author H.Yang
 * @date 2021/1/7
 */
@Slf4j
public class EnvAutoConfiguration {

    public static boolean isLocal = false;
    public static boolean isDev = false;
    public static boolean isTest = false;
    public static boolean isUat = false;
    public static boolean isFormal = false;

    /**
     * 不能通过new初始化
     */
    private EnvAutoConfiguration() {
    }

    /**
     * 判断运行环境
     *
     * @param active
     */
    public static void setActive(String active) {
        switch (active) {
            case EnvConstants.ENV_DEV:
                isDev = true;
                break;
            case EnvConstants.ENV_TEST:
                isTest = true;
                break;
            case EnvConstants.ENV_UAT:
                isUat = true;
                break;
            case EnvConstants.ENV_PROD:
                isFormal = true;
                break;
            default:
                isLocal = true;
        }
    }

    /**
     * 推断环境
     *
     * @param ctx
     */
    public static void deduceEnvironment(ConfigurableApplicationContext ctx) {
        deduceEnvironment(ctx.getEnvironment());
    }

    public static void deduceEnvironment(ConfigurableEnvironment environment) {
        log.debug("=== deduceEnv from spring.profiles.active ===");
        String active = environment.getProperty("spring.profiles.active");
        setActive(active);
    }

}
