package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.MessageContentApplication;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.infrastructure.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/7/13
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/message")
public class MessageContentController {

    @Resource
    private MessageContentApplication messageContentApplication;

    @ApiOperation("根据群ID查询会话消息")
    @GetMapping("/listChatRoom/{chatId}")
    Result<MessageContent> listChatRoom(@PathVariable("chatId") String chatId, @RequestParam(required = false) Integer seq) {

        return messageContentApplication.listByChatId(chatId, seq);
    }
}
