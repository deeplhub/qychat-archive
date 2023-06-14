package com.xh.qychat.infrastructure.properties;

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
    private Integer corePoolSize = this.getProcessors() + 1;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize = this.getProcessors() * 4;

    /**
     * 队列最大长度
     */
    private Integer queueCapacity = this.getProcessors() * 10;

    /**
     * 线程池前缀
     */
    private String threadNamePrefix = "CustomizedTaskExecutor-";

    /**
     * 线程的空闲时间，单位：秒
     */
    private Integer keepAliveSeconds = 60;

    /**
     * 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     */
    private Boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。单位：秒
     */
    private Integer awaitTerminationSeconds = 60;

    /**
     * 通过Runtime方法来获取当前服务器cpu内核，根据cpu内核来创建核心线程数和最大线程数
     * <p>
     * 配置线程个数：
     * 如果是CPU密集型任务，那么线程池的线程个数应该尽量少一些，一般为CPU的个数+1条线程(大量计算)
     * 如果是IO密集型任务，那么线程池的线程可以放的很大，如2*CPU的个数(IO操作)
     *
     * @return
     */
    public int getProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }
}
