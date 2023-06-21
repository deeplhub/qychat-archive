package com.xh.qychat.domain.task.event;

import com.xh.qychat.domain.task.event.async.ChatRoomCallable;
import com.xh.qychat.infrastructure.config.CustomizedTaskExecutor;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
public class ChatRoomEvent {
    private static CustomizedTaskExecutor taskExecutor;

    public static void createTaskExecutor() {
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;

        taskExecutor = SpringBeanUtils.getBean(CustomizedTaskExecutor.class);
        taskExecutor.setCorePoolSize(threadNum); // 核心线程数
        taskExecutor.initialize();
    }

    public static List<ChatRoomModel> listChatRoomDetail(Set<String> roomids) {
        long beginTime = System.currentTimeMillis();
        QyChatAdapter qychatAdapter = SpringBeanUtils.getBean("qyChatAdapterImpl");
        List<Future<ChatRoomModel>> futureList = roomids.parallelStream().map(roomid -> taskExecutor.submit(new ChatRoomCallable(roomid, qychatAdapter))).collect(Collectors.toList());

        List<ChatRoomModel> listData = futureList.parallelStream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("多线程处理数据时发生异常", e);
                throw new RuntimeException("多线程处理数据时发生异常");
            }
        }).collect(Collectors.toList());

        log.info("多线程执行耗时：{}", System.currentTimeMillis() - beginTime);

        return listData;
    }

}
