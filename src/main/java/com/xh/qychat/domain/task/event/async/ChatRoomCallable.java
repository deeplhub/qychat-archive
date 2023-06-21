package com.xh.qychat.domain.task.event.async;

import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
@AllArgsConstructor
public class ChatRoomCallable implements Callable<ChatRoomModel> {

    private final String roomid;
    private final QyChatAdapter qychatAdapter;

    @Override
    public ChatRoomModel call() {
        ChatRoomModel chatRoomDetail = qychatAdapter.getChatRoomDetail(roomid);

        log.info("线程 [{}] 执行获取 [{}] 群详情完成.", Thread.currentThread().getName(), roomid);
        return chatRoomDetail;
    }
}
