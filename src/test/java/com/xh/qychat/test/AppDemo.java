package com.xh.qychat.test;

import cn.hutool.json.JSONUtil;
import com.xh.qychat.infrastructure.config.CustomizedTaskExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
public class AppDemo {

    public static void test1(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        for (String str : listData) {
            log.info(str);
        }
        log.info("示例1：执行耗时：{}", System.currentTimeMillis() - beginTime);
    }

    public static void test2(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        listData.parallelStream().forEach(System.out::println);
        log.info("示例2：执行耗时：{}", System.currentTimeMillis() - beginTime);
    }

    public static void test3(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        listData.parallelStream().forEach(str -> log.info(str));
        log.info("示例3：执行耗时：{}", System.currentTimeMillis() - beginTime);
    }

    public static void test4(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        CustomizedTaskExecutor taskExecutor = new CustomizedTaskExecutor();
        int threadNum = Runtime.getRuntime().availableProcessors() + 1;
        taskExecutor.setCorePoolSize(threadNum); // 核心线程数
        taskExecutor.initialize();

        for (String str : listData) {
            taskExecutor.submit(() -> log.info(str));
        }
        log.info("示例4：执行耗时：{}", System.currentTimeMillis() - beginTime);
        taskExecutor.shutdown();
    }

    public static void test5(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        CustomizedTaskExecutor taskExecutor = new CustomizedTaskExecutor();
        int threadNum = Runtime.getRuntime().availableProcessors() + 1;
        taskExecutor.setCorePoolSize(threadNum); // 核心线程数
        taskExecutor.initialize();

        listData.parallelStream().forEach(str -> taskExecutor.submit(() -> log.info(str)));
        log.info("示例5：执行耗时：{}", System.currentTimeMillis() - beginTime);
        taskExecutor.shutdown();
    }


    public static void test6(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        // CPU内核数+1
        int threadSize = Runtime.getRuntime().availableProcessors() + 1;
        // 数据大小
        int dataSize = listData.size();
        // 方法一：批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / threadSize + 1;
        System.out.println(batchSize);

        // 方法二：批次大小（每个线程要处理数据量）
        int batchSize2 = dataSize / threadSize + 1;
        if (dataSize % threadSize == 0) {
            batchSize2 = batchSize2 - 1;
        }
        System.out.println(batchSize2);


        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);
        System.out.println(batchCount);

        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);
            List<String> batchData = listData.subList(start, end);
            log.info("第{}批次：start={}, end={}, batchSize={}, batchData={}", i, start, end, batchData.size(), JSONUtil.toJsonStr(batchData));
        }
        log.info("示例6：执行耗时：{}", System.currentTimeMillis() - beginTime);
    }


    public static void test7(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        // CPU内核数+1
        int threadSize = Runtime.getRuntime().availableProcessors() + 1;
        // 数据大小
        int dataSize = listData.size();
        // 方法一：批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / threadSize + 1;
        System.out.println(batchSize);

        // 方法二：批次大小（每个线程要处理数据量）
//        int batchSize2 = dataSize / threadSize + 1;
//        if (dataSize % threadSize == 0) {
//            batchSize2 = batchSize2 - 1;
//        }
//        System.out.println(batchSize2);


        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);
        System.out.println(batchCount);

        // 初始化线程池
        ExecutorService executorService = new ThreadPoolExecutor(batchCount,// 核心线程池大小
                100,// 线程池最大容量大小
                0L,// 线程池空闲时，线程存活的时间
                TimeUnit.MILLISECONDS,// 时间单位
                new LinkedBlockingQueue<Runnable>()// 任务队列
        );


        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);

            // 批次数据
            List<String> batchData = listData.subList(start, end);
            log.info("第{}批次：start={}, end={}, batchSize={}", i, start, end, batchData.size());
            executorService.submit(() -> {
                log.info(JSONUtil.toJsonStr(batchData));
            });
        }
        executorService.shutdown();
        log.info("示例7：执行耗时：{}", System.currentTimeMillis() - beginTime);
    }


    public static void test8(List<String> listData) throws Exception {
        long beginTime = System.currentTimeMillis();
        // CPU内核数+1
        int threadSize = Runtime.getRuntime().availableProcessors() + 1;
        // 数据大小
        int dataSize = listData.size();
        // 批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / threadSize + 1;
        System.out.println(batchSize);

        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);
        System.out.println(batchCount);

        // 初始化线程池
        ExecutorService executorService = new ThreadPoolExecutor(batchCount,// 核心线程池大小
                100,// 线程池最大容量大小
                0L,// 线程池空闲时，线程存活的时间
                TimeUnit.MILLISECONDS,// 时间单位
                new LinkedBlockingQueue<Runnable>()// 任务队列
        );


        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);

            // 批次数据
            List<String> batchData = listData.subList(start, end);
            log.info("第{}批次：start={}, end={}, batchSize={}", i, start, end, batchData.size());
            Future<Boolean> future = executorService.submit(() -> {
                for (String str : batchData) {
                    log.info(JSONUtil.toJsonStr(batchData));
                }
                return true;
            });

            // 返回每个线程处理的结果（顺序）
            log.info(future.get() + "");
        }
        executorService.shutdown();
    }

    public static void test9(List<String> listData) throws Exception {
        long beginTime = System.currentTimeMillis();
        // CPU内核数+1
        int threadSize = Runtime.getRuntime().availableProcessors() + 1;
        // 数据大小
        int dataSize = listData.size();
        // 批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / threadSize + 1;
        System.out.println(batchSize);

        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);
        System.out.println(batchCount);

        // 初始化线程池
        ExecutorService executorService = new ThreadPoolExecutor(batchCount,// 核心线程池大小
                100,// 线程池最大容量大小
                0L,// 线程池空闲时，线程存活的时间
                TimeUnit.MILLISECONDS,// 时间单位
                new LinkedBlockingQueue<Runnable>()// 任务队列
        );
        // 定义一个任务集合
        List<Callable<Boolean>> tasks = new ArrayList<>();

        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);

            // 批次数据
            List<String> batchData = listData.subList(start, end);
            log.info("第{}批次：start={}, end={}, batchSize={}", i, start, end, batchData.size());
            Future<Boolean> future = executorService.submit(() -> {
                for (String str : batchData) {
                    log.info(JSONUtil.toJsonStr(batchData));
                }
                return true;
            });

            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(() -> {
                for (String str : batchData) {
                    log.info(JSONUtil.toJsonStr(batchData));
                }
                return true;
            });

        }
        List<Future<Boolean>> futures = executorService.invokeAll(tasks);
        for (Future<Boolean> future : futures) {
            // 返回每个线程处理的结果（顺序）
            log.info(future.get() + "");
        }
        executorService.shutdown();
    }

    public static void test10(List<String> listData) {
        long beginTime = System.currentTimeMillis();
        // CPU内核数+1
        int threadSize = Runtime.getRuntime().availableProcessors() + 1;
        // 数据大小
        int dataSize = listData.size();
        // 方法一：批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / threadSize + 1;
        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);

        // 初始化线程池
        ExecutorService executorService = new ThreadPoolExecutor(batchCount,// 核心线程池大小
                100,// 线程池最大容量大小
                0L,// 线程池空闲时，线程存活的时间
                TimeUnit.MILLISECONDS,// 时间单位
                new LinkedBlockingQueue<Runnable>()// 任务队列
        );

        List<String> tempList = new ArrayList<>(batchSize);
        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);

            // 批次数据
            List<String> batchData = listData.subList(start, end);
            log.info("第{}批次：start={}, end={}, batchSize={}", i, start, end, batchData.size());
            executorService.execute(() -> {
                tempList.addAll(batchData);
            });
        }
        executorService.shutdown();
        log.info("示例10：执行耗时：{}", System.currentTimeMillis() - beginTime);
        log.info("影响行数：{}", tempList.size());
    }


    public static void main(String[] args) throws Exception {
        List<String> listData = getListData();
//        long beginTime = System.currentTimeMillis();

//        test1(listData);// 执行耗时：1993
//        test2(listData);// 执行耗时：3539
//        test3(listData);// 执行耗时：2531
//        test4(listData);// 执行耗时：1453
//        test5(listData);// 执行耗时：1419

//        test6(listData);// 执行耗时：67
//        test7(listData);// 执行耗时：42
//        test8(listData);
        test10(listData);

    }


    private static List<String> getListData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 34; i++) {
            list.add("demo_" + i);
        }

        return list;
    }

}
