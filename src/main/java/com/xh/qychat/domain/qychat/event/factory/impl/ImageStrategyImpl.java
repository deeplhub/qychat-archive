package com.xh.qychat.domain.qychat.event.factory.impl;

import cn.hutool.json.JSONObject;
import com.xh.qychat.domain.qychat.event.factory.MessageStrategy;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class ImageStrategyImpl implements MessageStrategy {

    @Override
    public void process(ChatDataModel model, MessageContentEntity entity) {
        JSONObject imageObject = model.getImage();

        JSONObject mediaObject = new JSONObject();

        mediaObject.putOpt("mediaFile", imageObject.getStr("sdkfileid"));// 媒体资源的id信息
        mediaObject.putOpt("mediaSize", imageObject.getInt("filesize"));// 图片资源的文件大小

        entity.setContent(mediaObject.toString());
        entity.setMediaStatus(1);
    }
}
