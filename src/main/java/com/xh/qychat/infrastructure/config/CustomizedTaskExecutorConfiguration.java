package com.xh.qychat.infrastructure.config;

import com.xh.qychat.infrastructure.properties.ThreadPoolProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定制任务执行线程池
 * <p>
 * 注意：使用注解 Async 开启异步 必须指定参数<>taskExecutor</> 如果不添加这个bean名称，会使用默认的线程池
 *
 * <p>
 * 参考地址：
 * https://cloud.tencent.com/developer/article/1467049
 * <p>
 * <p>
 * EnableAsync(proxyTargetClass = true)说明：
 * proxy-target-class属性值决定是基于接口的还是基于类的代理被创建。
 * 如果proxy-target-class 属性值被设置为true，那么基于类的代理将起作用（这时需要cglib库）。
 * 如果proxy-target-class属值被设置为false或者这个属性被省略，那么标准的JDK 基于接口的代理
 *
 * @author H.Yang
 * @date 2020/1/7
 */
@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class CustomizedTaskExecutorConfiguration {

    private final ThreadPoolProperties threadPoolProperties;

    @Bean
    public CustomizedTaskExecutor customizedTaskExecutor() {
        this.showSystem();
        this.showMemoryInfo();
        log.info("初始化线程池 Bean 'customizedTaskExecutor'");

        CustomizedTaskExecutor executor = new CustomizedTaskExecutor();

        // 核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        // 配置队列大小
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        // 空闲存活时间，单位：秒：当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());

        /*
            rejection-policy：当pool已经达到max size的时候，如何处理新任务
            CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
            线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
            当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；
            如果执行程序已关闭，则会丢弃该任务
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.getWaitForTasksToCompleteOnShutdown());
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());
        executor.initialize();

        return executor;
    }

    /**
     * 显示内存信息
     * <p>
     * Java 虚拟机的内存系统
     * <p>
     * init约等于xms的值，max约等于xmx的值
     * used是已经被使用的内存大小，committed是当前可使用的内存大小（包括已使用的）
     * committed >= used。committed不足时jvm向系统申请，若超过max则发生OutOfMemoryError错误
     * 机器内存小于192M,堆内存最大为二分之一
     * 机器内存大于等于1024M，堆内存最大为四分之一
     * JVM初始分配的内存由-Xms指定，默认是物理内存的1/64；
     * JVM最大分配的内存由-Xmx指定，默认是物理内存的1/4。
     * 默认空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制；
     * 空余堆内存大于70%时，JVM会减少堆直到-Xms的最小限制。
     * 因此服务器一般设置-Xms、-Xmx相等以避免在每次GC后调整堆的大小。
     * </p>
     */
    private void showMemoryInfo() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = mem.getHeapMemoryUsage();

        log.info("Heap committed: {}MB, init: {}MB, max: {}MB, used: {}MB", heap.getCommitted() / 1024 / 1024, heap.getInit() / 1024 / 1024, heap.getMax() / 1024 / 1024, heap.getUsed() / 1024 / 1024);
    }

    /**
     * 显示系统信息
     */
    private void showSystem() {
        OperatingSystemMXBean op = ManagementFactory.getOperatingSystemMXBean();
        log.info("System Architecture:[{}], Number of processors:[{}], System name:[{}], System version:[{}]", op.getArch(), op.getAvailableProcessors(), op.getName(), op.getVersion());
    }
}
