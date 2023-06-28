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
 * 视频消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class VideoMessageStrategyImpl implements MessageStrategy {

    private static final String FILE_NAME_PREFIX = "video/";
    private static final String FILE_NAME_SUFFIX = ".mp4";

    @Resource
    private QyChatAdapter qychatAdapter;

    @Override
    public String process(MediaMessage mediaMessage) {
        if (StrUtil.isBlank(mediaMessage.getContent())) return null;
        return JSONUtil.toJsonStr(this.getImageObject(mediaMessage));
    }


    public MediaMessage getImageObject(MediaMessage mediaMessage) {
        if (mediaMessage.getFileSize() > CommonConstants.MAX_FILE_SIZE) {
            mediaMessage.setNote("视频文件大小超出系统限制.");
            return mediaMessage;
        }

        String fileName = this.getFileName(mediaMessage.getMd5sum());
        qychatAdapter.download(mediaMessage.getSdkfileid(), fileName);

        mediaMessage.setFileName(fileName);
        mediaMessage.setStatus(true);
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
