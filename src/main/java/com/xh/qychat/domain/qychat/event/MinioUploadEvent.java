package com.xh.qychat.domain.qychat.event;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.infrastructure.config.EnvAutoConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author H.Yang
 * @date 2023/8/10
 */
@Slf4j
public class MinioUploadEvent {

    private static final String BUCKET_NAME = "qychat-archive";

    private static String OSS_URL = "http://192.168.88.32:7500/minio/upload/qychat-archive";

    static {
        if (EnvAutoConfiguration.isFormal) OSS_URL = "";
    }

    public static String upload(String fileName) {
        File file = new File(fileName);
        Map<String, Object> map = new HashMap<>();

        map.put("appName", BUCKET_NAME);
        map.put("file", file);

        try {
            log.info("上传对象存储，请求地址：{}，请求参数{}", OSS_URL, JSONUtil.toJsonStr(map));
            HttpResponse execute = HttpRequest.post(OSS_URL)
                    .contentType("multipart/form-data;charset=UTF-8")
                    .form(map)
                    .execute();

            log.info("上传对象存储，响应码：{}", execute.getStatus());

            if (execute.getStatus() == 200) {
                JSONObject jsonObject = JSONUtil.parseObj(execute.body());
                log.info("上传对象存储，响应结果：{}", jsonObject);

                if (jsonObject.getInt("errcode") == 200 && jsonObject.getJSONObject("datas") != null) {
                    return jsonObject.getJSONObject("datas").getStr("url");
                }
            }
        } catch (Exception e) {
            log.error("上传对象存储失败", e);
        } finally {
            file.delete();
        }
        return null;
    }
}
