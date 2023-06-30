package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.domain.qychat.service.strategy.dto.ChatDataMessageDTO;
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
    public String process(ChatDataMessageDTO chatDataDto) {
        if (chatDataDto.getFilesize() > CommonConstants.MAX_FILE_SIZE) {
            chatDataDto.setNote("文件大小超出系统限制.");
            chatDataDto.setMediaStatus(3);
            return JSONUtil.toJsonStr(chatDataDto);
        }

        String fileName = this.getFileName(chatDataDto);
        qychatAdapter.download(chatDataDto.getSdkfileid(), fileName);

        chatDataDto.setFilename(fileName);
        chatDataDto.setMediaStatus(2);

        return JSONUtil.toJsonStr(chatDataDto);
    }

    private String getFileName(ChatDataMessageDTO chatDataDto) {
        String filePath = CommonConstants.RESOURCES_PATH + chatDataDto.getType() + "/";

        // 判断文件夹是否存在，不存在则创建
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) folder.mkdirs();

        return filePath + chatDataDto.getFileNameSuffix();
    }
}
