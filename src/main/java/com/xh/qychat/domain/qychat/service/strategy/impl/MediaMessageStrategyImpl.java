package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
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
        JSONObject response = new JSONObject();
        if (chatDataDto.getFilesize() > CommonConstants.MAX_FILE_SIZE) {
            response.putOpt("note", "文件大小超出系统限制.");
            chatDataDto.setMediaStatus(3);
            return JSONUtil.toJsonStr(chatDataDto);
        }

        String fileName = this.getFileName(chatDataDto);
        qychatAdapter.download(chatDataDto.getSdkfileid(), fileName);

        response.putOpt("filename", fileName);
        response.putOpt("filesize", chatDataDto.getFilesize());

        chatDataDto.setMediaStatus(2);

        return response.toString();
    }

    private String getFileName(ChatDataMessageDTO chatDataDto) {
        String filePath = CommonConstants.RESOURCES_PATH + chatDataDto.getMsgType() + "/";

        // 判断文件夹是否存在，不存在则创建
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) folder.mkdirs();

        return filePath + chatDataDto.getFileNameSuffix();
    }
}
