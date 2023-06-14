package com.xh.qychat.infrastructure.integration.qychat.adapter.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wework.Finance;
import com.xh.qychat.infrastructure.constants.CacheConstants;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.constants.QychatConstants;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.task.ChatDataTask;
import com.xh.qychat.infrastructure.properties.QyChatProperties;
import com.xh.qychat.infrastructure.redis.impl.JedisPoolRepository;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Component
@AllArgsConstructor
public class QyChatAdapterImpl implements QyChatAdapter {

    private final QyChatProperties chatProperties;
    private final ChatDataTask chatDataTask;


    /**
     * 获取accessToken
     *
     * @param corpid
     * @param secret
     * @param key
     * @return
     */
    private static String getAccessToken(String corpid, String secret, String key) {
        log.info("获取 TOKEN...");
        JedisPoolRepository jedisPoolRepository = SpringBeanUtils.getBean(JedisPoolRepository.class);

        String accessToken = jedisPoolRepository.get(key);

        if (StrUtil.isNotBlank(accessToken)) return accessToken;

        JSONObject jsonObject = new JSONObject();

        jsonObject.putOpt("corpid", corpid);
        jsonObject.putOpt("corpsecret", secret);

        try {
            log.debug("获取 TOKEN ，请求地址：{}，请求参数：{}", QychatConstants.TOKEN_URL, jsonObject);
            String data = HttpUtil.get(QychatConstants.TOKEN_URL, jsonObject);
            log.debug("获取 TOKEN ，响应结果：{}", data);

            if (StrUtil.isBlank(data)) throw new RuntimeException("获取 TOKEN 异常");

            jsonObject = JSONUtil.parseObj(data);

            accessToken = jsonObject.getStr("access_token");
            if (StrUtil.isBlank(accessToken)) throw new RuntimeException("获取 TOKEN 异常");

            jedisPoolRepository.setex(key, accessToken, CacheConstants.DEFAULT_EXPIRE_TIME);
        } catch (RuntimeException e) {
            log.error("获取accessToken出错：" + e);
            throw new RuntimeException("获取accessToken出错!");
        }

        return accessToken;
    }


    @Override
    public List<ChatDataModel> listChatData(Long seq) {
        log.debug("获取 {} 开始的会话内容....", seq);

        // 获取sdk对象，首次使用初始化
        long sdk = Finance.NewSdk();
        // 初始化
        Finance.Init(sdk, chatProperties.getCorpid(), chatProperties.getSecret());

        try {
            // 获取聊天数据
            List<ChatDataModel> chatDatalList = this.pageSecretChatData(seq, sdk, new ArrayList<>());

            // 解密已获取的会话记录数据
            chatDatalList = this.process(sdk, chatDatalList);

            return chatDatalList;
        } catch (Exception e) {
            log.error("获取会话记录数据异常", e);
            throw new RuntimeException("获取会话记录数据异常");
        } finally {
            // 释放sdk，和NewSdk成对使用
            Finance.DestroySdk(sdk);
        }

    }

    /**
     * 递归获取聊天数据
     * <p>
     * 获取的数据是加密数据
     *
     * @param seq          消息序号
     * @param sdk
     * @param chatDataList 聊天数据
     * @return
     */
    private List<ChatDataModel> pageSecretChatData(Long seq, Long sdk, List<ChatDataModel> chatDataList) {
        log.debug("消息序号：{}，获取会话记录数据...", seq);
        long slice = Finance.NewSlice();

        try {
            // 获取会话记录数据
            int ret = Finance.GetChatData(sdk, seq, chatProperties.getLimit(), null, null, chatProperties.getTimeout(), slice);
            if (ret != 0) {
                throw new RuntimeException("getchatdata ret：" + ret);
            }

            // 获取消息
            String content = Finance.GetContentFromSlice(slice);
            ChatDataModel chatDataModel = JSONUtil.toBean(content, ChatDataModel.class);
            if (chatDataModel.getErrcode() != 0) {
                log.error("获取会话记录数据失败，异常码：{}", chatDataModel.getErrmsg());
                throw new RuntimeException("获取会话记录数据失败，异常码：" + chatDataModel.getErrmsg());
            }

            List<ChatDataModel> chatList = chatDataModel.getChatdata();
            log.debug("已获取会话记录：{}", chatList.size());

            if (!chatList.isEmpty()) {
                chatDataModel = chatList.get(chatList.size() - 1);

                if (chatDataList != null) {
                    chatDataList.addAll(chatList);
                }
                seq = chatDataModel.getSeq() + 1;
                // 递归
                this.pageSecretChatData(seq, sdk, chatDataList);
            }

            log.debug("获取会话总记录：{}", chatDataList.size());
            // 递归
            return chatDataList;
        } catch (Exception e) {
            log.error("获取原始会话记录数据异常", e);
            throw new RuntimeException("获取原始会话记录数据异常", e);
        } finally {
            // 释放slice，和NewSlice成对使用
            Finance.FreeSlice(slice);
        }
    }


    /**
     * 线程异步处理
     *
     * @param sdk
     * @param secretChatDataList
     * @return
     */
    private List<ChatDataModel> process(Long sdk, List<ChatDataModel> secretChatDataList) {
        log.debug("解密已获取的会话记录数据...");

        long beginTime = System.currentTimeMillis();
        List<ChatDataModel> listData = new ArrayList<>(secretChatDataList.size());
        List<Future<List<ChatDataModel>>> futureList = this.exec(sdk, secretChatDataList);

        try {
            for (Future<List<ChatDataModel>> future : futureList) {
                listData.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("多线程处理数据时发生异常", e);
            throw new RuntimeException("多线程处理数据时发生异常");
        }
        log.info("多线程执行耗时：{}", System.currentTimeMillis() - beginTime);

        return listData;
    }


    private List<Future<List<ChatDataModel>>> exec(Long sdk, List<ChatDataModel> secretChatDataList) {
        int dataSize = secretChatDataList.size();
        int threadNum = Runtime.getRuntime().availableProcessors() + 1;
        int chunkSize = dataSize / threadNum;

        List<Future<List<ChatDataModel>>> futureList = new ArrayList<>();
        List<ChatDataModel> cutListData = null;

        // 将数组的每个区段分配到不同的线程处理
        for (int i = 0; i < threadNum; i++) {
            if (dataSize < (i + 1)) {
                continue;
            }
            if (i == threadNum - 1) {
                cutListData = secretChatDataList.subList(chunkSize * i, dataSize);
            } else {
                cutListData = secretChatDataList.subList(chunkSize * i, chunkSize * (i + 1));
            }

            // 线程异常解密数据
            futureList.add(chatDataTask.handle(sdk, cutListData));
        }

        return futureList;
    }


    @Override
    public ChatRoomModel getChatRoomDetail(String roomid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("chat_id", roomid);

        String result = null;
        try {
            String accessToken = this.getAccessToken(chatProperties.getCorpid(), chatProperties.getSecret(), QychatConstants.QYCHAT_TOKEN_KEY);
            String uri = QychatConstants.GROUP_CHAT_GET_URL + accessToken;
            log.info("获取客户群详情，请求地址：{}，请求参数：{}", uri, jsonObject);
            result = HttpUtil.post(uri, jsonObject.toString());
            log.info("获取客户群详情，响应结果：{}", result);
        } catch (Exception e) {
            String format = StrUtil.format("客户群ID：{}，获取客户群详情异常", roomid);
            log.error(format, e);
            throw new RuntimeException(format);
        }

        ChatRoomModel room = JSONUtil.toBean(result, ChatRoomModel.class);

        if (room.getErrcode() != 0) {
            log.warn("获取客户群详情，解析异常 errcode：" + room.getErrmsg());
            return null;
        }

        room = room.getGroup_chat();

        if (StrUtil.isBlank(room.getChat_id())) {
            log.warn(room.getChat_id() + " - 未知的客户群详情");
            return null;
        }

        return room;
    }


    @Override
    public void download(String sdkfileid, String fileName) {
        log.debug("下载会话记录文件...");

        int ret = 0;
        long sdk = Finance.NewSdk();
        // 初始化
        Finance.Init(sdk, chatProperties.getCorpid(), chatProperties.getSecret());
        String indexbuf = "";

        try {
            while (true) {
                long mediaData = Finance.NewMediaData();
                ret = Finance.GetMediaData(sdk, indexbuf, sdkfileid, null, null, 3, mediaData);
                if (ret != 0) {
                    return;
                }
                log.debug("文件名称:{}, len:{}, data_len:{}, 状态(1完成、0未完成):{}", fileName, Finance.GetIndexLen(mediaData), Finance.GetDataLen(mediaData), Finance.IsMediaDataFinish(mediaData));

                try {
                    FileOutputStream outputStream = new FileOutputStream(new File(fileName), true);
                    outputStream.write(Finance.GetData(mediaData));
                    outputStream.close();
                } catch (Exception e) {
                    log.error("{} 文件写入磁盘异常.", fileName);
                }

                if (Finance.IsMediaDataFinish(mediaData) == 1) {
                    Finance.FreeMediaData(mediaData);
                    break;
                } else {
                    indexbuf = Finance.GetOutIndexBuf(mediaData);
                    Finance.FreeMediaData(mediaData);
                }
            }
            log.debug("{} 下载完毕.", fileName);
        } finally {
            Finance.DestroySdk(sdk);
        }

    }

}
