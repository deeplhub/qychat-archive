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
 * 图片消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class ImageMessageStrategyImpl implements MessageStrategy {

    private static final String FILE_NAME_PREFIX = "image/";
    private static final String FILE_NAME_SUFFIX = ".gif";
    @Resource
    private QyChatAdapter qychatAdapter;

    /**
     * 请求报文：
     * <pre>
     * {"md5sum":"50de8e5ae8ffe4f1df7a93841f71993a","filesize":70961,"sdkfileid":"CtYBMzA2OTAyMDEwMjA0NjIzMDYwMDIwMTAwMDIwNGI3ZmU0MDZlMDIwMzBmNTliMTAyMDQ1YzliNTQ3NzAyMDQ1YzM3M2NiYzA0MjQ2NjM0MzgzNTM0NjEzNTY1MmQzNDYxMzQzODJkMzQzMTYxNjEyZDM5NjEzOTM2MmQ2MTM2NjQ2NDY0NjUzMDY2NjE2NjM1MzcwMjAxMDAwMjAzMDExNTQwMDQxMDUwZGU4ZTVhZThmZmU0ZjFkZjdhOTM4NDFmNzE5OTNhMDIwMTAyMDIwMTAwMDQwMBI4TkRkZk1UWTRPRGcxTVRBek1ETXlORFF6TWw4eE9UUTVOamN6TkRZMlh6RTFORGN4TWpNNU1ERT0aIGEwNGQwYWUyM2JlYzQ3NzQ5MjZhNWZjMjk0ZTEyNTkz"}
     * </pre>
     * <p>
     * 参数说明：
     * <ul>
     * <li>msgtype：图片消息为：image</li>
     * <li>sdkfileid：媒体资源的id信息</li>
     * <li>md5sum：图片资源的md5值，供进行校验</li>
     * <li>filesize：图片资源的文件大小</li>
     * </ul>
     *
     * @param mediaMessage
     * @return
     */
    @Override
    public String process(MediaMessage mediaMessage) {
        if (StrUtil.isBlank(mediaMessage.getContent())) return null;
        return JSONUtil.toJsonStr(this.getImageObject(mediaMessage));
    }


    public MediaMessage getImageObject(MediaMessage mediaMessage) {
        if (mediaMessage.getFileSize() > CommonConstants.MAX_FILE_SIZE) {
            mediaMessage.setNote("图片文件大小超出系统限制.");
            mediaMessage.setMediaStatus(3);
            return mediaMessage;
        }

        String fileName = this.getFileName(mediaMessage.getMd5sum());
        qychatAdapter.download(mediaMessage.getSdkfileid(), fileName);

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
