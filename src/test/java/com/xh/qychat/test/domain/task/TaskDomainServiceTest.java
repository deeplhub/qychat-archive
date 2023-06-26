package com.xh.qychat.test.domain.task;

import com.xh.qychat.domain.task.service.TaskDomainService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashSet;
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
        roomids.add("wrgQjpQAAAbuqJrTx96r1Z9W35Y0Qihg");

//        List<ChatRoomModel> list = taskDomainService.listChatRoomDetail(roomids);
//
//        log.info("chatroom list:{}", JSONUtil.toJsonStr(list));

    }
}
