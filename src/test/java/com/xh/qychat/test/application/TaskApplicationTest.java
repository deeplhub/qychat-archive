package com.xh.qychat.test.application;

import com.xh.qychat.application.TaskApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author H.Yang
 * @date 2023/6/16
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class TaskApplicationTest {

    @Resource
    private TaskApplication taskApplication;

    @Test
    public void pullChatData() {
        taskApplication.pullChatData();
    }
}