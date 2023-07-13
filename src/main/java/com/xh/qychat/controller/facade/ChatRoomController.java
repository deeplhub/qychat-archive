package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.ChatRoomApplication;
import com.xh.qychat.infrastructure.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/12
 */

@Slf4j
@RestController
@RequestMapping("/chatRoom")
public class ChatRoomController {

    @Resource
    private ChatRoomApplication chatRoomApplication;

    /**
     * 拉取指定群详情
     *
     * @param roomId
     * @return
     */
    @GetMapping("/pullChatRoom/{roomId}")
    Result pullChatRoom(@PathVariable("roomId") String roomId) {

        return chatRoomApplication.pullChatRoom(roomId);
    }


    /**
     * 查询所有群列表
     *
     * @return
     */
    @GetMapping("/listChatRoom")
    Result listChatRoom() {

        return chatRoomApplication.listChatRoom();
    }

}
