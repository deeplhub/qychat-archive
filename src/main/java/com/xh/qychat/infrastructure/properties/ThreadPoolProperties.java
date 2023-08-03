package com.xh.qychat.infrastructure.properties;

import com.xh.qychat.infrastructure.constants.CommonConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 定义异步任务执行线程池参数配置
 *
 * @author H.Yang
 * @date 2021/12/21
 */
@Data
@ConfigurationProperties(prefix = "task.pool")
public class ThreadPoolProperties {

    /**
     * 核心线程数
     */
    private Integer corePoolSize = CommonConstants.CPU_INTENSIVE_THREAD_SIZE;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize = (int) (CommonConstants.AVAILABLE_PROCESSORS / (1 - 0.8));

    /**
     * 队列最大长度
     */
    private Integer queueCapacity = (int) (CommonConstants.AVAILABLE_PROCESSORS / (1 - 0.6));

    /**
     * 线程池前缀
     */
    private String threadNamePrefix = "common-task-worker-";

    /**
     * 线程的空闲时间，单位：秒
     */
    private Integer keepAliveSeconds = 40;

    /**
     * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。单位：秒
     */
    private Integer awaitTerminationSeconds = 60;

}
