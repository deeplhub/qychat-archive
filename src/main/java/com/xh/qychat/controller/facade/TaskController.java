package com.xh.qychat.controller.facade;

import com.xh.qychat.application.service.TaskApplication;
import com.xh.qychat.controller.schedule.ScheduleTask;
import com.xh.qychat.infrastructure.common.model.Result;
import com.xh.qychat.infrastructure.integration.qychat.properties.ChatDataProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.concurrent.ForkJoinPool;

/**
 * 定时任务
 *
 * @author H.Yang
 * @date 2023/6/16
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TaskController {

    @Resource
    private TaskApplication taskApplication;
    @Resource
    private ScheduleTask scheduleTask;

    /**
     * 拉取聊天数据
     *
     * @return
     */
    @GetMapping("/pullChatData")
    Result pullChatData() {

        return taskApplication.pullChatData();
    }

    @GetMapping("/pauseTask")
    Result pauseTask() {
        boolean status = !scheduleTask.isStatus();
        scheduleTask.setStatus(status);

        return Result.succeed(status ? "启动任务" : "暂停任务");
    }


//    /**
//     * 拉取群详情
//     *
//     * @return
//     */
//    @GetMapping("/pullChatRoom")
//    Result pullChatRoom() {
//
//        return taskApplication.pullChatRoom();
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
//    @GetMapping("/wxWorkPush")
//    String verify() {
//        // 解析 url 上的参数值
//        Map<String, String> parameterMap = RequestContextHolderUtils.getParameterMap();
//        log.info("RequestContextHolder：{}", JSONUtil.toJsonStr(parameterMap));
//
//        ChatDataProperties.Receive receive = chatDataProperties.getReceive();
//        WXMsgCrypt crypt = new WXMsgCrypt(receive.getToken(), receive.getEncodingAesKey(), chatDataProperties.getCorpid());
//        String echostr = crypt.verifyURL(
//                parameterMap.get("msg_signature"),
//                parameterMap.get("timestamp"),
//                parameterMap.get("nonce"),
//                parameterMap.get("echostr")
//        );
//        log.info("Verify URL：{}", echostr);
//
//        return echostr;
//    }


    /**
     * 接收消息请
     * <p>
     * 用户回复消息或者点击事件响应时，企业会收到回调消息，此消息是经过企业微信加密之后的密文以post形式发送给企业，
     * <p>
     * 企业收到post请求之后:
     * 1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce)
     * 2.验证消息体签名的正确性。
     * 3.将post请求的数据进行json解析，并将"encrypt"标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档
     * 第2，3步可以用企业微信提供的库函数DecryptMsg来实现。
     *
     * @param body
     * @return
     */
//    @PostMapping("/wxWorkPush")
//    String weChatPush(@RequestBody String body) {
//        log.info("body：{}", body);
//        // 解析 url 上的参数值
//        Map<String, String> parameterMap = RequestContextHolderUtils.getParameterMap();
//        log.info("RequestContextHolder：{}", JSONUtil.toJsonStr(parameterMap));
//
//        JSONObject jsonObject = JSONUtil.parseFromXml(body);
//        jsonObject = jsonObject.getJSONObject("xml");
//
//        ChatDataProperties.Receive receive = chatDataProperties.getReceive();
//        WXMsgCrypt crypt = new WXMsgCrypt(receive.getToken(), receive.getEncodingAesKey(), chatDataProperties.getCorpid());
//
//        String s2 = crypt.decrypt(
//                parameterMap.get("msg_signature"),
//                parameterMap.get("timestamp"),
//                parameterMap.get("nonce"),
//                jsonObject.getStr("Encrypt")
//        );
//
//
//        return "";
//    }
}
