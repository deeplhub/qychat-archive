package com.xh.qychat.domain.task.event;

import com.xh.qychat.infrastructure.config.CustomizedTaskExecutor;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
public class ChatRoomEvent {
    private CustomizedTaskExecutor taskExecutor;
    private QyChatAdapter qychatAdapter;

    private ChatRoomEvent() {
    }

    public static ChatRoomEvent getSingleton() {
        return Inner.instance;
    }

    public static ChatRoomEvent getTaskExecutor() {
        return getSingleton().createTaskExecutor();
    }

    private static class Inner {
        private static final ChatRoomEvent instance = new ChatRoomEvent();
    }

    public ChatRoomEvent setChatAdapter(QyChatAdapter qychatAdapter) {
        this.qychatAdapter = qychatAdapter;
        return this;
    }

    private ChatRoomEvent createTaskExecutor() {
        this.taskExecutor = SpringBeanUtils.getBean(CustomizedTaskExecutor.class);
        this.taskExecutor.setCorePoolSize(CommonConstants.IO_INTENSIVE_THREAD_SIZE);
        this.taskExecutor.initialize();
        return this;
    }

    public List<ChatRoomModel> listChatRoomDetail(Set<String> roomids) {
        long beginTime = System.currentTimeMillis();
        List<Future<List<ChatRoomModel>>> futureList = exec(new ArrayList<>(roomids));

        List<ChatRoomModel> listData = new ArrayList<>(roomids.size());
        try {
            for (Future<List<ChatRoomModel>> future : futureList) {
                listData.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("多线程处理数据时发生异常", e);
            throw new RuntimeException("多线程处理数据时发生异常");
        }

        taskExecutor.shutdown();
        log.info("多线程执行耗时：{}", System.currentTimeMillis() - beginTime);

        return listData;
    }


    private List<Future<List<ChatRoomModel>>> exec(List<String> roomids) {
        // 数据大小
        int dataSize = roomids.size();
        // 批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / CommonConstants.IO_INTENSIVE_THREAD_SIZE + 1;
        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);

        List<Future<List<ChatRoomModel>>> futureList = new ArrayList<>();

        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);

            // 批次数据
            List<String> batchData = roomids.subList(start, end);
            log.info("第{}批次：start={}, end={}, batchSize={}", i, start, end, batchData.size());


            Future<List<ChatRoomModel>> future = taskExecutor.submit(() -> {
                List<ChatRoomModel> roomModels = batchData.stream().map(roomid -> qychatAdapter.getChatRoomDetail(roomid)).collect(Collectors.toList());
                log.info("线程 [{}] 执行获取群详情完成.", Thread.currentThread().getName());
                return roomModels;
            });

            futureList.add(future);
        }

        return futureList;
    }

}
