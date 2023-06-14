package com.xh.qychat.domain.qychat.factory;

import cn.hutool.core.util.StrUtil;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Component
public class MessageFactory {

    public static List<MessageContentEntity> createMessageContentEntity(List<ChatDataModel> dataModelList) {
        List<MessageContentEntity> entityList = new ArrayList<>(dataModelList.size());

        dataModelList.parallelStream().forEach(item -> {
            MessageContentEntity entity = new MessageContentEntity();
            entity.setSeq(item.getSeq());
            entity.setMsgid(item.getMsgid());
            entity.setAction(item.getAction());
            entity.setFromid(item.getFrom());

            if ("send" .equals(item.getAction())) {
                entity.setTolist(item.getTolist());
                entity.setRoomid(StrUtil.isNotBlank(item.getRoomid()) ? item.getRoomid() : null);
                entity.setMsgtype(item.getMsgtype());
                entity.setContent(item.getText());
            } else if ("switch" .equals(item.getAction())) {
                entity.setMsgtype("switch");
            } else {
                entity.setMsgtype("revoke");
            }

            Date date = item.getMsgtime() != null ? new Date(item.getMsgtime()) : new Date();
            entity.setMsgtime(date);

            entityList.add(entity);
        });

        return entityList;
    }

    private String getContent(ChatDataModel model) {
        String content = null;
        if (StrUtil.isNotBlank(model.getImage())) {
            content = model.getImage();
        } else if (StrUtil.isNotBlank(model.getWeapp())) {
            content = model.getWeapp();
        } else if (StrUtil.isNotBlank(model.getRedpacket())) {
            content = model.getRedpacket();
        } else if (StrUtil.isNotBlank(model.getFile())) {
            content = model.getFile();
        } else if (StrUtil.isNotBlank(model.getVideo())) {
            content = model.getVideo();
        } else if (StrUtil.isNotBlank(model.getVoice())) {
            content = model.getVoice();
        } else if (StrUtil.isNotBlank(model.getChatrecord())) {
            content = model.getChatrecord();
        }

        return content;
    }
}
