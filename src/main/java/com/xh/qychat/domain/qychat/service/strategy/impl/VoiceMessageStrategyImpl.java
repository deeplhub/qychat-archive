package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.MediaMessage;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * 语音消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class VoiceMessageStrategyImpl implements MessageStrategy {
    private static final String FILE_NAME_PREFIX = "voice/";
    private static final String FILE_NAME_SUFFIX = ".mp3";


    @Resource
    private QyChatAdapter qychatAdapter;

    @Override
    public String process(MediaMessage mediaMessage) {
        if (StrUtil.isBlank(mediaMessage.getContent())) return null;

        return JSONUtil.toJsonStr(this.getVoiceObject(mediaMessage));
    }


    public MediaMessage getVoiceObject(MediaMessage mediaMessage) {
        int voiceSize = mediaMessage.getVoiceSize();
        if (voiceSize > CommonConstants.MAX_FILE_SIZE) {
            mediaMessage.setNote("音频文件大小超出系统限制.");
            mediaMessage.setMediaStatus(3);
            return mediaMessage;
        }

        String fileName = this.getFileName(mediaMessage.getMd5sum());
        qychatAdapter.download(mediaMessage.getSdkfileid(), fileName);

        mediaMessage.setFileSize(voiceSize);
        mediaMessage.setFileName(fileName);
        mediaMessage.setStatus(true);
        mediaMessage.setMediaStatus(2);
        return mediaMessage;
    }


    private String getFileName(String md5sum) {
        String fileName = CommonConstants.RESOURCES_PATH + FILE_NAME_PREFIX;

        // 判断文件夹是否存在，不存在则创建
        File folder = new File(fileName);
        if (!folder.exists() && !folder.isDirectory()) folder.mkdirs();

        return fileName + md5sum + FILE_NAME_SUFFIX;
    }
}
