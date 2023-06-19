package com.xh.qychat.domain.qychat.event.factory.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.event.factory.MessageStrategy;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatDataModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 混合消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class MixedStrategyImpl implements MessageStrategy {

    @Override
    public void process(ChatDataModel model, MessageContentEntity entity) {
        JSONObject jsonObject = model.getMixed();

        List<JSONObject> list = jsonObject.getBeanList("item", JSONObject.class);

        List<Map<String, Object>> linkedList = new LinkedList<>();
        Map<String, Object> map = null;

        for (JSONObject o : list) {
            map = new HashMap<>();
            map = this.getMedia(o, map);
            map = this.getText(o, map);

            linkedList.add(map);
        }

        entity.setContent(JSONUtil.toJsonStr(linkedList));
    }

    private Map<String, Object> getText(JSONObject jsonObject, Map<String, Object> map) {
        if ("text".equals(jsonObject.getStr("type"))) {
            JSONObject textObject = jsonObject.get("content", JSONObject.class);

            map.put("content", textObject.getStr("content"));
        }

        map.put("type", jsonObject.getStr("type"));
        return map;
    }

    private Map<String, Object> getMedia(JSONObject jsonObject, Map<String, Object> map) {
        if ("image".equals(jsonObject.getStr("type"))) {
            JSONObject mediaObject = jsonObject.get("content", JSONObject.class);

            map.put("mediaFile", mediaObject.getStr("sdkfileid"));
            map.put("mediaSize", mediaObject.getInt("filesize"));
        }

        map.put("type", jsonObject.getStr("type"));
        return map;
    }
}
