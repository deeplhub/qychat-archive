package com.xh.qychat.infrastructure.config;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 定制的任务执行器
 * <p>
 * 这是{@link ThreadPoolTaskExecutor}的一个简单替换，可搭配TransmittableThreadLocal实现父子线程之间的数据传递
 * <p>
 * 显示线程执行情况：任务总数、已完成数、活跃线程数，队列大小信息
 *
 * @author H.Yang
 * @date 2020/1/7
 */
@Slf4j
public class CommonTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        super.execute(ttlRunnable);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        super.execute(ttlRunnable, startTimeout);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Callable ttlCallable = TtlCallable.get(task);
        return super.submit(ttlCallable);
    }

    @Override
    public Future<?> submit(Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submit(ttlRunnable);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submitListenable(ttlRunnable);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        Callable ttlCallable = TtlCallable.get(task);
        return super.submitListenable(ttlCallable);
    }

}
