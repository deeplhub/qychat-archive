package com.xh.qychat.domain.qychat.model.factory;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.domain.qychat.model.MessageContent;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息内容工厂类
 *
 * @author H.Yang
 * @date 2023/6/14
 */
public class MessageContentFactory {

    private static class Inner {
        private static final MessageContentFactory instance = new MessageContentFactory();
    }

    private MessageContentFactory() {
    }

    public static MessageContentFactory getSingleton() {
        return MessageContentFactory.Inner.instance;
    }


    public List<MessageContentEntity> createEntity(MessageContent messageContent) {
        List<ChatDataModel> dataModelList = messageContent.getDataModelList();

        return dataModelList.parallelStream().map(o -> this.getMessageContentEntity(o)).collect(Collectors.toList());
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

        this.getActionMessage(chatData, entity);
        return entity;
    }

    private void getActionMessage(ChatDataModel chatData, MessageContentEntity entity) {
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

        // 根据不同的类型选择不的策略
        MessageStrategy strategy = SpringBeanUtils.getBean(dataModel.getMsgtype() + "StrategyImpl");
        if (strategy != null) {
            strategy.process(dataModel, entity);
        }
    }

}
