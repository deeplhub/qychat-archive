package com.xh.qychat.test;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author H.Yang
 * @date 2023/6/21
 */
@Slf4j
public class AppDemo {

    private Map<String, Function<JSONObject, JSONObject>> actionMappings = new HashMap<>();

    {
        actionMappings.put("text", o -> this.getText(o));
        actionMappings.put("image", o -> this.getImage(o));
    }

    public static void main(String[] args) throws Exception {
        AppDemo appDemo = new AppDemo();

        appDemo.demo("text2");
    }

    public void demo(String type) {
        Function<JSONObject, JSONObject> function = actionMappings.get(type);
        JSONObject apply = function.apply(new JSONObject());

        System.out.println(apply);
    }


    private JSONObject getImage(JSONObject o) {
        o.putOpt("text", "这是一张图片");
        return o;
    }

    private JSONObject getText(JSONObject o) {
        o.putOpt("text", "这一个文本");
        return o;
    }
}
