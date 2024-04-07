package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.ChatRoomApplication;
import com.xh.qychat.domain.qychat.model.ChatRoom;
import com.xh.qychat.infrastructure.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/12
 */
@Api(tags = "群管理")
@RestController
@RequestMapping("/chatRoom")
public class ChatRoomController {

    @Resource
    private ChatRoomApplication chatRoomApplication;

    /**
     * 根据群ID查询群详情
     *
     * @param chatId
     * @return
     */
    @GetMapping("/pullChatRoom/{chatId}")
    Result pullChatRoom(@PathVariable("chatId") String chatId) {

        return chatRoomApplication.pullChatRoom(chatId);
    }

    @ApiOperation(value = "查询所有群列表")
    @GetMapping("/listChatRoom")
    Result<ChatRoom> listChatRoom() {

        return chatRoomApplication.listChatRoom();
    }

}
