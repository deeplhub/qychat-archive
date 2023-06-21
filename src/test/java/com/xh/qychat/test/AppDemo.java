package com.xh.qychat.test;

import com.xh.qychat.infrastructure.config.CustomizedTaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
public class AppDemo {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 101; i++) {
            list.add("demo_" + i);
        }

        int dataSize = list.size();
        int threadNum = Runtime.getRuntime().availableProcessors();
        int batchCount = (dataSize - 1) / threadNum + 1;

        CustomizedTaskExecutor executor = new CustomizedTaskExecutor();
        executor.setCorePoolSize(threadNum);// 核心线程数

//        executor.submit()
    }
}
