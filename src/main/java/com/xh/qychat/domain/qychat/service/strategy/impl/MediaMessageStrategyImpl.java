package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.adapter.impl.QyChatAdapterImpl;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;

import java.io.File;

/**
 * 媒体消息
 * 图片消息
 * 语音消息
 * 视频消息
 * 文件消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class MediaMessageStrategyImpl implements MessageStrategy {

    private final QyChatAdapter qychatAdapter = SpringBeanUtils.getBean(QyChatAdapterImpl.class);

    @Override
    public String process(ChatDataMessage chatDataMessage) {
        chatDataMessage.setStatus(false);
        if (chatDataMessage.getFileSize() > CommonConstants.MAX_FILE_SIZE) {
            chatDataMessage.setNote("文件大小超出系统限制.");
            chatDataMessage.setMediaStatus(3);
            return JSONUtil.toJsonStr(chatDataMessage);
        }

        String fileName = this.getFileName(chatDataMessage);
        qychatAdapter.download(chatDataMessage.getSdkfileid(), fileName);

        chatDataMessage.setFileName(fileName);
        chatDataMessage.setStatus(true);
        chatDataMessage.setMediaStatus(2);

        return JSONUtil.toJsonStr(chatDataMessage);
    }

    private String getFileName(ChatDataMessage chatDataMessage) {
        String filePath = CommonConstants.RESOURCES_PATH + chatDataMessage.getType() + "/";

        // 判断文件夹是否存在，不存在则创建
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) folder.mkdirs();

        return filePath + chatDataMessage.getFileNameSuffix();
    }
}
