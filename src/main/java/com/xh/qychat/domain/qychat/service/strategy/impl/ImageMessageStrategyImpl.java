//package com.xh.qychat.domain.qychat.service.strategy.impl;
//
//import cn.hutool.json.JSONObject;
//import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
//import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
//import com.xh.qychat.infrastructure.constants.CommonConstants;
//import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
//import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.File;
//
///**
// * 图片消息
// *
// * @author H.Yang
// * @date 2023/6/19
// */
//@Component
//public class ImageMessageStrategyImpl implements MessageStrategy {
//
//    @Resource
//    private QyChatAdapter qychatAdapter;
//
//    /**
//     * 请求报文：
//     * <pre>
//     * {"md5sum":"50de8e5ae8ffe4f1df7a93841f71993a","filesize":70961,"sdkfileid":"CtYBMzA2OTAyMDEwMjA0NjIzMDYwMDIwMTAwMDIwNGI3ZmU0MDZlMDIwMzBmNTliMTAyMDQ1YzliNTQ3NzAyMDQ1YzM3M2NiYzA0MjQ2NjM0MzgzNTM0NjEzNTY1MmQzNDYxMzQzODJkMzQzMTYxNjEyZDM5NjEzOTM2MmQ2MTM2NjQ2NDY0NjUzMDY2NjE2NjM1MzcwMjAxMDAwMjAzMDExNTQwMDQxMDUwZGU4ZTVhZThmZmU0ZjFkZjdhOTM4NDFmNzE5OTNhMDIwMTAyMDIwMTAwMDQwMBI4TkRkZk1UWTRPRGcxTVRBek1ETXlORFF6TWw4eE9UUTVOamN6TkRZMlh6RTFORGN4TWpNNU1ERT0aIGEwNGQwYWUyM2JlYzQ3NzQ5MjZhNWZjMjk0ZTEyNTkz"}
//     * </pre>
//     * <p>
//     * 参数说明：
//     * <ul>
//     * <li>msgtype：图片消息为：image</li>
//     * <li>sdkfileid：媒体资源的id信息</li>
//     * <li>md5sum：图片资源的md5值，供进行校验</li>
//     * <li>filesize：图片资源的文件大小</li>
//     * </ul>
//     *
//     * @param model
//     * @param entity
//     */
//    @Override
//    public void process(ChatDataModel model, MessageContentEntity entity) {
//        JSONObject imageObject = model.getImage();
//
//        JSONObject mediaObject = this.getObject(imageObject);
//
//        entity.setContent(mediaObject.toString());
//    }
//
//
//    public JSONObject getObject(JSONObject imageObject) {
//        String sdkfileid = imageObject.getStr("sdkfileid");
//        String md5sum = imageObject.getStr("md5sum");
//        int fileSize = imageObject.getInt("filesize");
//
//        JSONObject mediaObject = new JSONObject();
//
//        mediaObject.putOpt("fileSize", fileSize);
//        mediaObject.putOpt("status", false);
//
//
//        if (fileSize > CommonConstants.MAX_FILE_SIZE) {
//            mediaObject.putOpt("note", "图片大小超出系统限制.");
//            return mediaObject;
//        }
//
//        String fileName = this.getFileName(md5sum);
//        qychatAdapter.download(sdkfileid, fileName);
//
//
//        mediaObject.putOpt("fileName", fileName);
//        mediaObject.putOpt("status", true);
//        return mediaObject;
//    }
//
//
//    private String getFileName(String md5sum) {
//        String fileName = "/data/oss/image/";
//
//        // 判断文件夹是否存在，不存在则创建
//        File folder = new File(fileName);
//        if (!folder.exists() && !folder.isDirectory()) folder.mkdirs();
//
//        return fileName + md5sum + CommonConstants.GIF_SUFFIX;
//    }
//}
