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

            switch (item.getAction()) {
                case "switch":
                    entity.setMsgtype("switch");
                    break;
                case "revoke":
                    entity.setMsgtype("revoke");
                    break;
                case "send":
                    expand(item, entity);
                    break;
                default:
                    break;
            }
            entityList.add(entity);
        });

        return entityList;
    }

    private static void expand(ChatDataModel dataModel, MessageContentEntity entity) {
        entity.setTolist(dataModel.getTolist());
        entity.setRoomid(StrUtil.isNotBlank(dataModel.getRoomid()) ? dataModel.getRoomid() : null);
        entity.setMsgtype(dataModel.getMsgtype());
//        entity.setContent(item.getText());

        MessageStrategy strategy = SpringBeanUtils.getBean(dataModel.getMsgtype() + "StrategyImpl");
        if (strategy != null) {
            strategy.process(dataModel, entity);
        }
    }


//    private String getContent(ChatDataModel model) {
//        String content = null;
//        if (StrUtil.isNotBlank(model.getImage())) {
//            content = model.getImage();
//        } else if (StrUtil.isNotBlank(model.getWeapp())) {
//            content = model.getWeapp();
//        } else if (StrUtil.isNotBlank(model.getRedpacket())) {
//            content = model.getRedpacket();
//        } else if (StrUtil.isNotBlank(model.getFile())) {
//            content = model.getFile();
//        } else if (StrUtil.isNotBlank(model.getVideo())) {
//            content = model.getVideo();
//        } else if (StrUtil.isNotBlank(model.getVoice())) {
//            content = model.getVoice();
//        } else if (StrUtil.isNotBlank(model.getChatrecord())) {
//            content = model.getChatrecord();
//        }
//
//        return content;
//    }
}
