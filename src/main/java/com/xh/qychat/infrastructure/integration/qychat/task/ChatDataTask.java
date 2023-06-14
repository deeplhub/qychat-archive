package com.xh.qychat.infrastructure.integration.qychat.task;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tencent.wework.Finance;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.properties.QyChatProperties;
import com.xh.qychat.infrastructure.util.RsaUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@AllArgsConstructor
@Component
public class ChatDataTask {

    private final QyChatProperties qychatProperties;

    @Async("customizedTaskExecutor")
    public Future<List<ChatDataModel>> handle(Long sdk, List<ChatDataModel> cutListData) {
        List<ChatDataModel> list = new LinkedList();
        ChatDataModel chatData = null;

        for (ChatDataModel chatDataModel : cutListData) {
            chatData = this.decrypt(sdk, chatDataModel);
            if (chatData == null) continue;

            list.add(chatData);
        }
        log.info("解密会话记录数据完成.");
        return new AsyncResult<>(JSONUtil.toList(JSONUtil.toJsonStr(list), ChatDataModel.class));
    }


    private ChatDataModel decrypt(Long sdk, ChatDataModel data) {
        log.debug("消息ID：{}，开始解密...", data.getMsgid());
        byte[] decoderData = Base64.getDecoder().decode(data.getEncryptRandomKey());

        long newSlice = 0;
        try {
            byte[] decrypt = RsaUtils.decrypt(decoderData, RsaUtils.getPrivateKey(qychatProperties.getPrivateKey()));
            String encryptKey = new String(decrypt, CommonConstants.CHARSET_UTF8);

            if (StrUtil.isBlank(encryptKey)) {
                log.info("消息ID：{}，加密密钥为空", data.getMsgid());
                return null;
            }

            // 将获取到的数据进行解密操作
            newSlice = Finance.NewSlice();
            Finance.DecryptData(sdk, encryptKey, data.getEncryptChatMsg(), newSlice);
        } catch (Exception e) {
            log.error("解密失败！", e);
            return null;
        }

        // 解密后的消息
        ChatDataModel chatData = JSONUtil.toBean(Finance.GetContentFromSlice(newSlice), ChatDataModel.class);
        chatData.setSeq(data.getSeq());

        return chatData;
    }
}
