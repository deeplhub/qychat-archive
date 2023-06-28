package com.xh.qychat.test.domain.qychat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.qychat.domain.qychat.service.MessageContentDomain;
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
 * @date 2023/6/25
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageContentDomainTest {

    @Resource
    private MessageContentDomain messageContentDomain;

    @Test
    public void pageList() {

        Set<String> set = this.demo(1, 20, new HashSet<>());
        System.out.println();
    }

    @Test
    public void testTransactional() {
        messageContentDomain.demo();
        System.out.println();
        while (true) {
            System.out.print(".");
        }
    }


    public Set<String> demo(Integer pageNum, Integer limit, Set<String> roomIds) {
        Page<String> page = messageContentDomain.pageListRoomIdGoupByRoomId(pageNum, limit);

        List<String> list = page.getRecords();
        if (!list.isEmpty()) {
            roomIds.addAll(list);

            this.demo(pageNum + 1, limit, roomIds);
        }

        return roomIds;
    }
}