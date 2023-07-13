package com.xh.qychat.test.application;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.domain.qychat.event.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

    @Test
    public void pullChatRoom() {
        taskApplication.pullChatRoom();
    }

//    @Test
//    public void pullChatRoom2() {
//        taskApplication.pullChatRoom("wrgQjpQAAAMBX_ZlzFH0Vv9ePj7FnNQA");
//    }

//    @Test
//    public void pullPersonnel() {
//        taskApplication.pullPersonnel("AnChangZhiBan1");
//    }

    @Test
    public void test() {
        String json = "{\"item\":[{\"type\":\"image\",\"content\":\"{\\\"md5sum\\\":\\\"ca38ab27fb8fa3ae119395b734e5d29b\\\",\\\"filesize\\\":8146,\\\"sdkfileid\\\":\\\"CtQBMzA2ODAyMDEwMjA0NjEzMDVmMDIwMTAwMDIwNGVhZTk1ZTg4MDIwMzBmNDI0MTAyMDQ1M2MxNmViNDAyMDQ2NDhmZjVlMzA0MjQzNjYxNjIzOTY2NjY2NjM5MmQzNTMzMzEzMjJkMzQzMjM5NjEyZDM4MzYzOTMzMmQzMTMwMzc2MzM3Mzg2NjY2MzIzNDM2NjEwMjAxMDAwMjAyMWZlMDA0MTBjYTM4YWIyN2ZiOGZhM2FlMTE5Mzk1YjczNGU1ZDI5YjAyMDEwMTAyMDEwMDA0MDASOE5EZGZNVFk0T0RnMU9EQTVOak01TXpnMk5GOHhPVEl5TmprNU5EY3lYekUyT0RjeE5UWXhPVGs9GiBiYmVjM2FjZjRkMmIzZWQ5N2I3ZThiOWJlM2U3NzU5OA==\\\"}\"},{\"type\":\"text\",\"content\":\"{\\\"content\\\":\\\"有效期我选择了2023.07.19，为什么详情里还是2023.06.19\\\\n\\\"}\"},{\"type\":\"image\",\"content\":\"{\\\"md5sum\\\":\\\"ee01775cd6b2a7dcfd104487794c617c\\\",\\\"filesize\\\":9633,\\\"sdkfileid\\\":\\\"CtQBMzA2ODAyMDEwMjA0NjEzMDVmMDIwMTAwMDIwNGVhZTk1ZTg4MDIwMzBmNDI0MTAyMDQ1M2MxNmViNDAyMDQ2NDhmZjVlMzA0MjQzNDY1MzU2MTMxMzU2NTM1MmQzNTY1MzY2MTJkMzQzMDM5MzkyZDYxMzk2NTYxMmQzNzMzMzkzODYxNjE2MzM0MzczMTYyMzcwMjAxMDAwMjAyMjViMDA0MTBlZTAxNzc1Y2Q2YjJhN2RjZmQxMDQ0ODc3OTRjNjE3YzAyMDEwMTAyMDEwMDA0MDASOE5EZGZNVFk0T0RnMU9EQTVOak01TXpnMk5GOHhNemc0TWpjek1EZzRYekUyT0RjeE5UWXhPVGs9GiBlOGU2NzE1MDkyOGRkMjY3YTM2ZGNhODU2Yjc4YjE3Zg==\\\"}\"},{\"type\":\"text\",\"content\":\"{\\\"content\\\":\\\"询价编号：\\\\n2023061900028\\\"}\"}]}";
//        List<JSONObject> objectList = JSONUtil.toList(json, JSONObject.class);
        JSONObject jsonObject = JSONUtil.parseObj(json);


        ChatDataModel model = new ChatDataModel();
        model.setMixed(jsonObject);


        MessageContentEntity entity = new MessageContentEntity();

        MessageStrategy strategy = SpringBeanUtils.getBean("mixedStrategyImpl");
//        strategy.process(model, entity);


        System.out.println(JSONUtil.toJsonStr(entity.getContent()));
    }
}