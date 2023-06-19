package com.xh.qychat.domain.qychat.event;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.domain.qychat.event.factory.MessageStrategy;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public class MessageEvent {

    public static List<MessageContentEntity> create(List<ChatDataModel> dataModelList) {
        List<MessageContentEntity> entityList = new ArrayList<>(dataModelList.size());

        dataModelList.parallelStream().forEach(item -> {
            MessageContentEntity entity = new MessageContentEntity();

            entity.setSeq(item.getSeq());
            entity.setMsgid(item.getMsgid());
            entity.setAction(item.getAction());
            entity.setFromid(item.getFrom());
            entity.setPublickeyVer(item.getPublickeyVer());
            entity.setMsgtime(item.getMsgtime() != null ? new Date(item.getMsgtime()) : new Date());
            entity.setCreateTime(new Date());

            getActionMessage(item, entity);

            entityList.add(entity);
        });

        return entityList;
    }

    private static void getActionMessage(ChatDataModel item, MessageContentEntity entity) {
        switch (item.getAction()) {
            case "send": // 发送消息
                getSendMessage(item, entity);
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

    private static void getSendMessage(ChatDataModel dataModel, MessageContentEntity entity) {
        entity.setTolist(dataModel.getTolist());
        entity.setRoomid(StrUtil.isNotBlank(dataModel.getRoomid()) ? dataModel.getRoomid() : null);
        entity.setMsgtype(dataModel.getMsgtype());
        entity.setContent(dataModel.getContent());

        MessageStrategy strategy = SpringBeanUtils.getBean(dataModel.getMsgtype() + "StrategyImpl");
        if (strategy != null) {
            strategy.process(dataModel, entity);
        }
    }

}
