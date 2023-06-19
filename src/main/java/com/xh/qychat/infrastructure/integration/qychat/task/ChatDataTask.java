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
        log.debug("线程 [{}] 执行解密会话内容完成，共执行行数 [{}].", Thread.currentThread().getName(), list.size());
        return new AsyncResult<>(JSONUtil.toList(JSONUtil.toJsonStr(list), ChatDataModel.class));
    }


    /**
     * 解密会话内容
     *
     * @param sdk
     * @param data
     * @return
     */
    private ChatDataModel decrypt(Long sdk, ChatDataModel data) {
        byte[] decoderData = Base64.getDecoder().decode(data.getEncryptRandomKey());

        long newSlice = 0;
        try {
            // 密钥长度：2048 bit，密钥格式：PKCS#8，输出格式：PEM/Base64
            // 密钥在线生成：http://web.chacuo.net/netrsakeypair
            byte[] decrypt = RsaUtils.decrypt(decoderData, RsaUtils.getPrivateKey(qychatProperties.getPrivateKey()));
            String encryptKey = new String(decrypt, CommonConstants.CHARSET_UTF8);

            if (StrUtil.isBlank(encryptKey)) {
                log.debug("线程 [{}] 消息ID：{}，解密密钥为空", Thread.currentThread().getName(), data.getMsgid());
                return null;
            }

            // 将获取到的数据进行解密操作
            newSlice = Finance.NewSlice();
            Finance.DecryptData(sdk, encryptKey, data.getEncryptChatMsg(), newSlice);
        } catch (Exception e) {
            log.error("线程 [{}] 解密 [{}] 消息内容失败！", Thread.currentThread().getName(), data.getMsgid(), e);
            return null;
        }

        // 解密后的消息
        ChatDataModel chatData = JSONUtil.toBean(Finance.GetContentFromSlice(newSlice), ChatDataModel.class);
        chatData.setSeq(data.getSeq());
        chatData.setPublickeyVer(data.getPublickeyVer());

        return chatData;
    }
}
