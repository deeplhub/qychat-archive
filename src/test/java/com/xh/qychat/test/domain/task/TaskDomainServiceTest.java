package com.xh.qychat.test.domain.task;

import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.task.service.TaskDomainService;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskDomainServiceTest {

    @Resource
    private TaskDomainService taskDomainService;

    @Test
    public void listChatRoomDetail() {
        Set<String> roomids = new HashSet<>();
        roomids.add("wrgQjpQAAA19wAonp7c_ys_7m0HsWjXw");
        roomids.add("wrgQjpQAAAaFYrQK4kXgqebXxe-xH01w");
        roomids.add("wrgQjpQAAAAHkfoGc8g-iXpb6-6m_S_A");

        List<ChatRoomModel> list = taskDomainService.listChatRoomDetail(roomids);

        log.info("chatroom list:{}", JSONUtil.toJsonStr(list));

    }
}
