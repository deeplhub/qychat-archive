package com.xh.qychat.domain.qychat.model.factory;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.service.adapter.MessageAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
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


    public List<MessageContentEntity> createEntity(List<ChatDataModel> dataModels) {

        return dataModels.stream().map(o -> this.getMessageContentEntity(o)).collect(Collectors.toList());
    }

    private MessageContentEntity getMessageContentEntity(ChatDataModel chatData) {
        MessageContentEntity entity = new MessageContentEntity();

        entity.setSeq(chatData.getSeq());
        entity.setMsgid(chatData.getMsgid());
        entity.setAction(chatData.getAction());
        entity.setFromid(chatData.getFrom());
        entity.setPublickeyVer(chatData.getPublickeyVer());
        entity.setMsgtime(chatData.getMsgtime() != null ? new Date(chatData.getMsgtime()) : new Date());
        entity.setCreateTime(new Date());

        this.getAction(chatData, entity);

        return entity;
    }

    private void getAction(ChatDataModel chatData, MessageContentEntity entity) {
        switch (chatData.getAction()) {
            case "send": // 发送消息
                this.getSendMessage(chatData, entity);
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

    private void getSendMessage(ChatDataModel dataModel, MessageContentEntity entity) {
        entity.setTolist(dataModel.getTolist());
        entity.setRoomid(StrUtil.isNotBlank(dataModel.getRoomid()) ? dataModel.getRoomid() : null);
        entity.setMsgtype(dataModel.getMsgtype());
        entity.setContent(dataModel.getContent());
        entity.setMediaStatus(1);

        if (StrUtil.isBlank(dataModel.getMsgtype()) || StrUtil.isBlank(dataModel.getContent())) return;

//        MessageStrategy strategy = SpringBeanUtils.getBean(dataModel.getMsgtype() + "MessageStrategyImpl");
//        if (strategy != null) {
//            ChatDataMessage chatDataMessage = new ChatDataMessage();
//            chatDataMessage.setContent(dataModel.getContent());
//            chatDataMessage.setType(entity.getMsgtype());
//
//            chatDataMessage = chatDataMessage.create();
//
//            log.debug("消息ID：[{}], 消息请求：{}", entity.getMsgid(), JSONUtil.toJsonStr(chatDataMessage));
//            String content = strategy.process(chatDataMessage);
//            log.debug("消息ID：[{}], 消息策略返回结果：{}", entity.getMsgid(), content);
//
//            entity.setContent(content);
//            entity.setMediaStatus(chatDataMessage.getMediaStatus());
//        }

//        if ("image".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("text".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("markdown".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("emotion".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("mixed".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("file".equals(entity.getMsgtype())) {
//            return;
//        }
//
//        if ("voice".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("video".equals(entity.getMsgtype())) {
//            return;
//        }
//        if ("chatrecord".equals(entity.getMsgtype())) {
//            System.out.println();
//        }


        ChatDataMessage chatDataMessage = new ChatDataMessage();
        chatDataMessage.setContent(dataModel.getContent());
        chatDataMessage.setType(entity.getMsgtype());

        chatDataMessage = chatDataMessage.create();

        MessageAdapter messageAdapter = new MessageAdapter(entity.getMsgtype());

        log.debug("消息ID：[{}], 消息请求：{}", entity.getMsgid(), JSONUtil.toJsonStr(chatDataMessage));
        String content = messageAdapter.getChatDataMessage(chatDataMessage);
        log.debug("消息ID：[{}], 消息策略返回结果：{}", entity.getMsgid(), content);

        entity.setContent(content);
        entity.setMediaStatus(chatDataMessage.getMediaStatus());
    }

}
