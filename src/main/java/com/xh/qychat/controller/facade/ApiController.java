package com.xh.qychat.controller.facade;

import cn.hutool.json.JSONUtil;
import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.infrastructure.integration.qychat.properties.ChatDataProperties;
import com.xh.qychat.infrastructure.util.RequestContextHolderUtils;
import com.xh.qychat.infrastructure.util.wx.WXMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author H.Yang
 * @date 2023/6/16
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    private TaskApplication taskApplication;
    @Resource
    private ChatDataProperties chatDataProperties;

//    @GetMapping("/pullChatData")
//    Result pullChatData() {
//
//        return taskApplication.pullChatData();
//    }


    /**
     * 验证回调URL
     * <p>
     * 企业开启回调模式时，企业微信会向验证url发送一个get请求。企业收到类似请求：
     * GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
     * HTTP/1.1 Host: qy.weixin.qq.com
     * <p>
     * 接收到该请求时，企业应
     * 1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及企业微信推送过来的随机加密字符串(echostr),
     * 这一步注意作URL解码。
     * 2.验证消息体签名的正确性
     * 3.解密出echostr原文，将原文当作Get请求的response，返回给企业微信
     * 第2，3步可以用企业微信提供的库函数VerifyURL来实现。
     */
    @GetMapping("/wxWorkPush")
    String verify() {
        // 解析 url 上的参数值
        Map<String, Object> holderMap = RequestContextHolderUtils.getMapHolder();
        log.info("RequestContextHolder：{}", JSONUtil.toJsonStr(holderMap));

        String msgSignature = (String) holderMap.get("msg_signature");
        String timestamp = (String) holderMap.get("timestamp");
        String nonce = (String) holderMap.get("nonce");
        String echostr = (String) holderMap.get("echostr");

        ChatDataProperties.Receive receive = chatDataProperties.getReceive();
        WXMsgCrypt crypt = new WXMsgCrypt(receive.getToken(), receive.getEncodingAesKey(), chatDataProperties.getCorpid());
        echostr = crypt.verifyURL(msgSignature, timestamp, nonce, echostr);
        log.info("Verify URL：{}", echostr);

        return echostr;
    }


    @PostMapping("/wxWorkPush")
    String weChatPush(String body) {
        log.info("body：{}", body);
        // 解析 url 上的参数值
        Map<String, Object> holderMap = RequestContextHolderUtils.getMapHolder();
        log.info("RequestContextHolder：{}", JSONUtil.toJsonStr(holderMap));

        return "";
    }
}
