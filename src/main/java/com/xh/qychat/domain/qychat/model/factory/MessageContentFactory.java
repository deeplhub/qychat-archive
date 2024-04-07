package com.xh.qychat.domain.qychat.model.factory;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.domain.qychat.event.adapter.MessageAdapter;
import com.xh.qychat.domain.qychat.event.strategy.dto.ChatDataMessageDTO;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.repository.entity.MemberEntity;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息内容工厂类
 *
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
public class MessageContentFactory {

    private static class Inner {
        private static final MessageContentFactory instance = new MessageContentFactory();
    }

    private MessageContentFactory() {
    }

    public static MessageContentFactory getSingleton() {
        return MessageContentFactory.Inner.instance;
    }


    public List<MessageContentEntity> createEntity(List<MessageContent> messageContents) {

        return messageContents.stream().map(o -> this.getMessageContentEntity(o)).collect(Collectors.toList());
    }

    private MessageContentEntity getMessageContentEntity(MessageContent messageContents) {
        MessageContentEntity entity = new MessageContentEntity();

        entity.setSeq(messageContents.getSeq());
        entity.setMsgid(messageContents.getMsgid());
        entity.setAction(messageContents.getAction());
        entity.setFromid(messageContents.getFromid());
        entity.setPublickeyVer(messageContents.getPublickeyVer());
        entity.setMsgtime(messageContents.getMsgtime());
        entity.setCreateTime(new Date());

        this.getAction(messageContents, entity);

        return entity;
    }

    private void getAction(MessageContent messageContents, MessageContentEntity entity) {
        switch (messageContents.getAction()) {
            case "send": // 发送消息
                this.getSendMessage(messageContents, entity);
                break;
            case "revoke": // 撤回消息
                entity.setMsgtype("revoke");
                break;
            case "switch": // 切换企业日志
                entity.setMsgtype("switch");
                break;
            default:
                break;
        }

    }

    private void getSendMessage(MessageContent messageContent, MessageContentEntity entity) {
        entity.setTolist(messageContent.getTolist());
        entity.setRoomid(messageContent.getRoomid());
        entity.setMsgtype(messageContent.getMsgtype());
        //entity.setOriginalContent(messageContent.getContent());
        entity.setMediaStatus(1);

        if (StrUtil.isBlank(messageContent.getMsgtype()) || StrUtil.isBlank(messageContent.getContent())) {
            return;
        }
        ChatDataMessageDTO chatDataDto = new ChatDataMessageDTO();
        chatDataDto.setBody(messageContent.getContent());
        chatDataDto.setMsgType(messageContent.getMsgtype());

        chatDataDto = chatDataDto.create();

        MessageAdapter messageAdapter = new MessageAdapter(messageContent.getMsgtype());

        log.debug("消息ID：[{}]，消息类型：{}，消息内容：{}", messageContent.getMsgid(), messageContent.getMsgtype(), messageContent.getContent());
        String content = messageAdapter.getChatDataMessage(chatDataDto);
        log.debug("消息ID：[{}]，消息策略返回结果：{}", messageContent.getMsgid(), content);

        entity.setContent(content);
        entity.setMediaStatus(chatDataDto.getMediaStatus());
    }


    public MessageContent toMessageContent(MessageContentEntity entity) {
        MessageContent messageContent = new MessageContent();

        messageContent.setId(entity.getId());
        messageContent.setSeq(entity.getSeq());
        messageContent.setAction(entity.getAction());
        messageContent.setFromid(entity.getFromid());
        messageContent.setMsgtime(entity.getMsgtime());
        messageContent.setMsgtype(entity.getMsgtype());
        messageContent.setContent(entity.getContent());

        return messageContent;
    }

    public MessageContent toMessageContent(MessageContentEntity entity, Map<String, MemberEntity> memberMap) {
        MessageContent messageContent = this.toMessageContent(entity);

        MemberEntity memberEntity = memberMap.get(entity.getFromid());
        if (memberEntity == null) {
            return messageContent;
        }

        messageContent.setMemberId(memberEntity.getId() + "");
        messageContent.setMemberName(memberEntity.getName());
        messageContent.setMemberAvatar(memberEntity.getAvatar());

        return messageContent;
    }
}
