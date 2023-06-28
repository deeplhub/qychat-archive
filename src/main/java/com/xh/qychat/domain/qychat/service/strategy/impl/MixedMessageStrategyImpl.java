package com.xh.qychat.domain.qychat.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xh.qychat.domain.qychat.repository.entity.MessageContentEntity;
import com.xh.qychat.domain.qychat.service.strategy.MessageStrategy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 混合消息
 *
 * @author H.Yang
 * @date 2023/6/19
 */
@Component
public class MixedMessageStrategyImpl implements MessageStrategy {

    private Map<String, Function<JSONObject, JSONObject>> actionMappings = new HashMap<>();

    @Resource
    private ImageMessageStrategyImpl imageMessageStrategy;

    {
        actionMappings.put("text", o -> this.getText(o));
        actionMappings.put("image", o -> this.getImage(o));
    }

    /**
     * 请求报文：
     *
     * @param entity
     */
    @Override
    public void process(MessageContentEntity entity) {
        if (StrUtil.isBlank(entity.getContent())) return;

        JSONObject jsonObject = JSONUtil.parseObj(entity.getContent());
        List<JSONObject> mixedList = jsonObject.getBeanList("item", JSONObject.class);
        List<JSONObject> collect = mixedList.parallelStream().map(this::getAction).filter(Objects::nonNull).collect(Collectors.toList());

        entity.setContent(JSONUtil.toJsonStr(collect));
    }

    private JSONObject getAction(JSONObject mixedObject) {
        String type = mixedObject.getStr("type");

        Function<JSONObject, JSONObject> function = actionMappings.get(type);
        if (function == null) return null;

        JSONObject jsonObject = function.apply(mixedObject);
        jsonObject.put("type", jsonObject.getStr("type"));
        return jsonObject;
    }


    private JSONObject getText(JSONObject textObject) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.putOpt("content", textObject.getJSONObject("content").getStr("content"));
        return jsonObject;
    }

    private JSONObject getImage(JSONObject imagebject) {
        imagebject = imagebject.getJSONObject("content");
        return imageMessageStrategy.getImageObject(imagebject);
    }

}
