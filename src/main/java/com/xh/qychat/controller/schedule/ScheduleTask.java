package com.xh.qychat.controller.schedule;

import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.infrastructure.redis.JedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/9/25
 */
@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class ScheduleTask {

    private volatile boolean status = true;

    @Resource
    private JedisRepository jedisRepository;
    @Resource
    private TaskApplication taskApplication;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    @Scheduled(cron = "0 0/2 * * * ?")
    void pullChatData() {
        if (!status) {
            log.info("pull chat data task is stop.");
            return;
        }

        String lock = "pause_pull_chat_data_task";
        try {
            boolean exist = jedisRepository.isExist(lock);
            if (exist) {
                return;
            }

            taskApplication.pullChatData();
        } finally {
            jedisRepository.del(lock);
        }
    }

}
