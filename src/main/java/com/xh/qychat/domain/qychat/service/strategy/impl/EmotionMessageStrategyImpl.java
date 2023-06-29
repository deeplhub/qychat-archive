package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.model.ChatDataMessage;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.adapter.impl.QyChatAdapterImpl;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 表情消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
public class EmotionMessageStrategyImpl implements MessageStrategy {

    private static final String FILE_NAME_PREFIX = "image/";
    private static final Map<Integer, String> EMOTION_MAP = new HashMap<>();

    static {
        EMOTION_MAP.put(1, ".gif");
        EMOTION_MAP.put(2, ".png");
    }

    private final QyChatAdapter qychatAdapter = SpringBeanUtils.getBean(QyChatAdapterImpl.class);

    @Override
    public String process(ChatDataMessage chatDataMessage) {
        chatDataMessage.setStatus(false);
        JSONObject contentObject = chatDataMessage.getContentObject();
        String fileNameSuffix = EMOTION_MAP.get(contentObject.getInt("type"));

        String fileName = this.getFileName(chatDataMessage.getMd5sum(), fileNameSuffix);
        qychatAdapter.download(chatDataMessage.getSdkfileid(), fileName);

        chatDataMessage.setFileName(fileName);
        chatDataMessage.setStatus(true);
        chatDataMessage.setMediaStatus(2);
        return JSONUtil.toJsonStr(chatDataMessage);
    }

    private String getFileName(String md5sum, String fileNameSuffix) {
        String fileName = CommonConstants.RESOURCES_PATH + FILE_NAME_PREFIX;

        // 判断文件夹是否存在，不存在则创建
        File folder = new File(fileName);
        if (!folder.exists() && !folder.isDirectory()) folder.mkdirs();

        return fileName + md5sum + fileNameSuffix;
    }
}
