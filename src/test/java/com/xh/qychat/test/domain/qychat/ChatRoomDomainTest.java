package com.xh.qychat.test.domain.qychat;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.domain.qychat.model.factory.ChatRoomFactory;
import com.xh.qychat.domain.qychat.repository.entity.ChatRoomEntity;
import com.xh.qychat.domain.qychat.repository.service.ChatRoomService;
import com.xh.qychat.domain.qychat.repository.service.impl.ChatRoomServiceImpl;
import com.xh.qychat.domain.qychat.service.ChatRoomDomain;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author H.Yang
 * @date 2023/6/25
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatRoomDomainTest {

    @Resource
    private ChatRoomDomain chatRoomDomain;

    @Test
    public void saveOrUpdateBatch() {
        FileReader fileReader = new FileReader("C:\\Users\\Andrew\\桌面\\model.txt");
        String json = fileReader.readString();

        List<ChatRoomModel> list = JSONUtil.toList(json, ChatRoomModel.class);

        boolean isSuccess = chatRoomDomain.saveOrUpdateBatch(new ChatRoom(list));

        System.out.println();
    }

    @Test
    public void listByChatId() {
        FileReader fileReader = new FileReader("C:\\Users\\Andrew\\桌面\\model.txt");
        String json = fileReader.readString();

        List<ChatRoomModel> list = JSONUtil.toList(json, ChatRoomModel.class);

        Set<String> chatIds = ChatRoomFactory.listChatId(new ChatRoom(list));

        ChatRoomService chatRoomService = SpringBeanUtils.getBean(ChatRoomServiceImpl.class);
        List<ChatRoomEntity> chatRoomEntities = chatRoomService.listByChatId(chatIds);
        Map<String, ChatRoomEntity> dictMap = chatRoomEntities.stream().collect(HashMap::new, (k, v) -> k.put(v.getChatId(), v), HashMap::putAll);
        System.out.println();

    }
}