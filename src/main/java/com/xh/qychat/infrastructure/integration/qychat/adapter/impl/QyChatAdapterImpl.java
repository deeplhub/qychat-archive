package com.xh.qychat.infrastructure.integration.qychat.adapter.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wework.Finance;
import com.xh.qychat.infrastructure.config.CommonTaskExecutor;
import com.xh.qychat.infrastructure.constants.CacheConstants;
import com.xh.qychat.infrastructure.constants.CommonConstants;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.constants.QychatConstants;
import com.xh.qychat.infrastructure.integration.qychat.model.*;
import com.xh.qychat.infrastructure.integration.qychat.properties.ChatDataProperties;
import com.xh.qychat.infrastructure.integration.qychat.properties.CustomerProperties;
import com.xh.qychat.infrastructure.redis.JedisRepository;
import com.xh.qychat.infrastructure.redis.impl.JedisPoolRepository;
import com.xh.qychat.infrastructure.util.RsaUtils;
import com.xh.qychat.infrastructure.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
@Slf4j
@Component
public class QyChatAdapterImpl implements QyChatAdapter {

    @Resource
    private ChatDataProperties chatProperties;
    @Resource
    private CustomerProperties customerProperties;


    private String getAccessToken(String corpid, String secret) {

        return this.getAccessToken(corpid, secret, QychatConstants.QYCHAT_TOKEN_KEY);
    }

    /**
     * 获取accessToken
     *
     * @param corpid
     * @param secret
     * @param key
     * @return
     */
    private String getAccessToken(String corpid, String secret, String key) {
        JedisRepository jedisPoolRepository = SpringBeanUtils.getBean(JedisPoolRepository.class);

        String accessToken = jedisPoolRepository.get(key);
        long keyExpire = jedisPoolRepository.getKeyExpire(key);

        // 当 token 不为空，且过期时间小于 5s 时生成新的 token
        // 原因：保证 token 有效性
        if (StrUtil.isNotBlank(accessToken) && keyExpire > 5) {
            return accessToken;
        }

        synchronized (this) {
            accessToken = jedisPoolRepository.get(key);
            if (StrUtil.isNotBlank(accessToken)) {
                return accessToken;
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.putOpt("corpid", corpid);
            jsonObject.putOpt("corpsecret", secret);

            try {
                log.debug("获取 TOKEN ，请求地址：{}，请求参数：{}", QychatConstants.TOKEN_URL, jsonObject);
                String data = HttpUtil.get(QychatConstants.TOKEN_URL, jsonObject);
                log.debug("获取 TOKEN ，响应结果：{}", data);

                if (StrUtil.isBlank(data)) {
                    throw new RuntimeException("获取 TOKEN 异常");
                }

                jsonObject = JSONUtil.parseObj(data);

                accessToken = jsonObject.getStr("access_token");
                if (StrUtil.isBlank(accessToken)) {
                    throw new RuntimeException("获取 TOKEN 异常");
                }

                jedisPoolRepository.setex(key, accessToken, CacheConstants.DEFAULT_EXPIRE_TIME);
            } catch (RuntimeException e) {
                log.error("获取accessToken出错：" + e);
                throw new RuntimeException("获取accessToken出错!");
            }
        }

        return accessToken;
    }


    @Override
    public List<ChatDataModel> listChatData(Long seq) {
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
                throw new RuntimeException("GetChatData ret：" + ret);
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

                chatDataList.addAll(chatList);

                seq = chatDataModel.getSeq() + 1;
                // 递归
                this.pageSecretChatData(seq, sdk, chatDataList);
            }

            log.debug("获取会话总记录：{}", chatDataList.size());
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
        List<ChatDataModel> listData = new ArrayList<>(secretChatDataList.size());
        List<Future<List<ChatDataModel>>> futureList = this.pieceExec(sdk, secretChatDataList);

        try {
            for (Future<List<ChatDataModel>> future : futureList) {
                listData.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("多线程处理数据时发生异常", e);
            throw new RuntimeException("多线程处理数据时发生异常");
        }
        return listData;
    }

    private List<Future<List<ChatDataModel>>> pieceExec(Long sdk, List<ChatDataModel> chatDataList) {
        // 数据大小
        int dataSize = chatDataList.size();
        // 批次大小（每个线程要处理数据量）
        int batchSize = (dataSize - 1) / CommonConstants.IO_INTENSIVE_THREAD_SIZE + 1;
        // 批次处理数
        int batchCount = (int) Math.ceil(1.0 * dataSize / batchSize);

        CommonTaskExecutor taskExecutor = SpringBeanUtils.getBean(CommonTaskExecutor.class);

        List<Future<List<ChatDataModel>>> futureList = new ArrayList<>();

        // 根据批次数遍历数据
        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, dataSize);

            // 批次数据
            List<ChatDataModel> batchData = chatDataList.subList(start, end);
            log.debug("第{}批次：start={}, end={}, batchSize={}", i, start, end, batchData.size());

            Future<List<ChatDataModel>> future = taskExecutor.submit(() -> batchData.parallelStream().map(o -> this.decrypt(sdk, o)).filter(Objects::nonNull).filter(o -> StrUtil.isNotBlank(o.getRoomid())).collect(Collectors.toList()));

            futureList.add(future);
        }

        return futureList;
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
            byte[] decrypt = RsaUtils.decrypt(decoderData, RsaUtils.getPrivateKey(chatProperties.getPrivateKey()));
            String encryptKey = new String(decrypt, StandardCharsets.UTF_8);

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


    @Override
    public void download(String sdkfileid, String fileName) {
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
                    FileOutputStream outputStream = new FileOutputStream(fileName, true);
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

    @Override
    public Set<String> listRoomId() {
        JSONObject body = new JSONObject();

        body.putOpt("status_filter", 0);// 客户群跟进状态过滤。默认为0
        body.putOpt("limit", 1000);// 分页，预期请求的数据量，取值范围 1 ~ 1000

        // 递归分页查询
        return this.listRoomId(body, new HashSet<>());
    }

    private Set<String> listRoomId(JSONObject bodyObject, Set<String> roomIds) {
        String accessToken = this.getAccessToken(customerProperties.getCorpid(), customerProperties.getSecret(), QychatConstants.QYCHAT_CUSTOMER_TOKEN_KEY);
        String uri = QychatConstants.CHAT_ROOM_ID_URL + accessToken;

        JSONObject jsonObject;
        try {
            log.info("获取客户群ID列表，请求地址：{}，请求参数：{}", uri, bodyObject);
            String result = HttpUtil.post(uri, bodyObject.toString());
            log.info("获取客户群详情，响应结果：{}", result);

            jsonObject = JSONUtil.toBean(result, JSONObject.class);
        } catch (Exception e) {
            log.error("获取客户群列表异常", e);
            throw new RuntimeException("获取客户群列表异常", e);
        }

        if (jsonObject.getInt("errcode") != 0) {
            throw new RuntimeException("获取客户群详情失败，异常码：" + jsonObject.getStr("errmsg"));
        }

        List<JSONObject> groupChatList = jsonObject.getBeanList("group_chat_list", JSONObject.class);
        Set<String> chatIds = groupChatList.stream().map(o -> o.getStr("chat_id")).collect(Collectors.toSet());

        if (!chatIds.isEmpty()) {
            roomIds.addAll(chatIds);
        }

        // 分页游标，下次请求时填写以获取之后分页的记录。如果该字段返回空则表示已没有更多数据
        String nextCursor = jsonObject.getStr("next_cursor");
        if (StrUtil.isNotBlank(nextCursor)) {
            bodyObject.putOpt("cursor", nextCursor);

            this.listRoomId(bodyObject, roomIds);
        }

        return roomIds;
    }

    @Override
    public ChatRoomModel getChatRoomDetail(String chatId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("chat_id", chatId);
        jsonObject.putOpt("need_name", 1);// 是否需要返回群成员的名字group_chat.member_list.name。0-不返回；1-返回。默认不返回

        String result;
        try {
            String accessToken = this.getAccessToken(customerProperties.getCorpid(), customerProperties.getSecret(), QychatConstants.QYCHAT_CUSTOMER_TOKEN_KEY);
            String uri = QychatConstants.CHAT_ROOM_DETAIL_URL + accessToken;

            log.info("获取 [{}] 客户群详情，请求地址：{}，请求参数：{}", chatId, uri, jsonObject);
            result = HttpUtil.post(uri, jsonObject.toString());
            log.info("获取 [{}] 客户群详情，响应结果：{}", chatId, result);
        } catch (Exception e) {
            String format = StrUtil.format("获取 [{}] 客户群详情异常", chatId);
            log.error(format, e);
            throw new RuntimeException(format);
        }

        ChatRoomModel room = JSONUtil.toBean(result, ChatRoomModel.class);

        if (room.getErrcode() != 0) {
            return null;
        }

        room = room.getGroupChat();

        if (StrUtil.isBlank(room.getChatId())) {
            return null;
        }

        return room;
    }

    @Override
    public MemberModel getMemberDetail(String userId) {
        MemberModel memberModel = JSONUtil.toBean(this.getMember(userId), MemberModel.class);

        if (memberModel.getErrcode() != 0) {
            log.warn("获取 [{}] 成员（内部联系人）详情，解析异常 errcode：{}", userId, memberModel.getErrmsg());
            return new MemberModel();
        }

        return memberModel;
    }

    @Override
    public CustomerModel getCustomerDetail(String userId) {
        CustomerModel customerModel = JSONUtil.toBean(this.getCustomer(userId), CustomerModel.class);

        if (customerModel.getErrcode() != 0) {
            log.warn("获取 [{}] 客户（外部联系人）详情，解析异常 errcode：{}", userId, customerModel.getErrmsg());
            return null;
        }

        // 外部联系人详情
        customerModel = customerModel.getExternalContact();
        if (customerModel != null) {
            return customerModel;
        }

        // 当客户不是联系人时会获取不到客户详情，需要用群详情中的用户ID生成未知客户
        customerModel.setExternalUserid(userId);
        customerModel.setName("外部未知客户");
        customerModel.setNote("当前客户未被添加为联系人，获取不到客户详情");

        return customerModel;
    }


    @Override
    public PersonnelModel getPersonnelDetail(String userId) {
        // 获取客户详情（外部联系人）
        if (userId.startsWith("wb") || userId.startsWith("wo") || userId.startsWith("wm")) {
            PersonnelModel model = JSONUtil.toBean(this.getCustomer(userId), PersonnelModel.class);
            if (model.getErrcode() != 0) {
                log.warn("获取 [{}] 客户（外部联系人）详情，解析异常 errcode：{}", userId, model.getErrmsg());
                return null;
            }

            // 外部联系人详情
            model = model.getExternalContact();

            // 当客户不是联系人时会获取不到客户详情，需要用群详情中的用户ID生成未知客户，类型为1
            model.setUserid(userId);
            model.setName("外部未知客户");
            model.setNote("当前客户未被添加为联系人，获取不到客户详情");

            return model;
        }

        // 获取成员详情（内部联系人）
        PersonnelModel model = JSONUtil.toBean(this.getMember(userId), PersonnelModel.class);
        if (model.getErrcode() != 0) {
            log.warn("获取 [{}] 成员（内部联系人）详情，解析异常 errcode：{}", userId, model.getErrmsg());
            return new PersonnelModel();
        }

        return model;
    }

    private String getMember(String userId) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.putOpt("access_token", this.getAccessToken(chatProperties.getCorpid(), chatProperties.getSecret()));
        jsonObject.putOpt("userid", userId);

        String result;
        try {
            log.info("获取 [{}] 成员（内部联系人）详情，请求地址：{}，请求参数：{}", userId, QychatConstants.MEMBER_DETAIL_URL, jsonObject);
            result = HttpUtil.get(QychatConstants.MEMBER_DETAIL_URL, jsonObject);
            log.info("获取 [{}] 成员（内部联系人）详情，响应结果：{}", userId, result);
        } catch (Exception e) {
            String format = StrUtil.format("获取 [{}] 成员（内部联系人）详情异常", userId);
            log.error(format, e);
            throw new RuntimeException(format);
        }
        return result;
    }

    private String getCustomer(String userId) {
        String accessToken = this.getAccessToken(customerProperties.getCorpid(), customerProperties.getSecret(), QychatConstants.QYCHAT_CUSTOMER_TOKEN_KEY);

        JSONObject jsonObject = new JSONObject();

        jsonObject.putOpt("access_token", accessToken);
        jsonObject.putOpt("external_userid", userId);// 外部联系人的userid，注意不是企业成员的帐号

        String result;
        try {
            log.info("获取 [{}] 客户（外部联系人）详情，请求地址：{}，请求参数：{}", userId, QychatConstants.CUSTOMER_DETAIL_URL, jsonObject);
            result = HttpUtil.get(QychatConstants.CUSTOMER_DETAIL_URL, jsonObject);
            log.info("获取 [{}] 客户（外部联系人）详情，响应结果：{}", userId, result);
        } catch (Exception e) {
            String format = StrUtil.format("获取 [{}] 客户（外部联系人）详情异常", userId);
            log.error(format, e);
            throw new RuntimeException(format);
        }
        return result;
    }
}
