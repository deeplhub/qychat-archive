package com.xh.qychat.test.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wework.Finance;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.model.MemberModel;
import com.xh.qychat.infrastructure.integration.qychat.model.PersonnelModel;
import com.xh.qychat.infrastructure.integration.qychat.properties.ChatDataProperties;
import com.xh.qychat.infrastructure.util.wx.WXMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/6/12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class QyChatApiTest {

    @Resource
    private ChatDataProperties qychatDataProperties;
    @Resource
    private QyChatAdapter qychatAdapter;

    @Test
    public void getChatData() {
        long sdk = Finance.NewSdk();
        // 初始化
        Finance.Init(sdk, "wwb55a50126edc83d2", "1jCvYfSyNF2gYeRT6gxNz9_1RK7GNY8mv6-26JrOjK0");
        long slice = Finance.NewSlice();
        int ret = Finance.GetChatData(sdk, 0, 1000, null, null, 30, slice);
        // 获取消息
        String data = Finance.GetContentFromSlice(slice);
        System.out.println();
    }

    @Test
    public void getChatRoomDetail() {
        ChatRoomModel chatRoomDetail = qychatAdapter.getChatRoomDetail("wrgQjpQAAAhSGCNKPd_I3hJ5vFvGFogA");
        System.out.println();
    }

    @Test
    public void getMemberDetail() {
        MemberModel memberDetail = qychatAdapter.getMemberDetail("AnChangZhiBan1");
        System.out.println(memberDetail);
    }

    @Test
    public void getCustomerDetail() {
        qychatAdapter.getCustomerDetail("wmgQjpQAAAWS26_1UIwGOKwpB7idJ4ug");
    }

    @Test
    public void getPersonnelDetail() {
        PersonnelModel detail = qychatAdapter.getPersonnelDetail("wmgQjpQAAAdhRlUt4NoZl_t6aad3Kj6Q");
        System.out.println(JSONUtil.toJsonStr(detail));
    }

    @Test
    public void listRoomId() {

        qychatAdapter.listRoomId();

    }

    public static void main(String[] args) {
        String corpid = "wwb55a50126edc83d2";
        String token = "2JJYzvrzaZ2Kcqyh4";
        String encodingAESKey = "S3Y1UxBb7LIxMDYtNm9CkQmyhVQ7xqwIXxyayrvRPMl";


        String msgSignature = "fdea7cc6d2013518448b810e5ca37fa804717b63";
        String timestamp = "1687624986";
        String nonce = "1687252722";

        String body = "<xml><ToUserName><![CDATA[wwb55a50126edc83d2]]></ToUserName><Encrypt><![CDATA[KDSEeBdaiQq3bHY7wkhXg7Vo994kBSkABR7gJoiYvslq8WTsry3vmRPXnN7dzvYpYyAf4KH4DsPxK0J070GrXKp3rVnQMr6J4NCXx+xi0Lz45a4okZA3R2hVVuMr47IKkAkRLOPOnV/fLbJo9UHnEVSQBlBS/0te5ewBMslIsh2D7yh/XYKZ7xglVWlnnMBAkbarncR5pqYZeGK/njSwpbfY5NfGcQrE6UsW8f389c/kP/VxYjB0uAaqTKZ/HwGU/keGKfGKNpL1baV7LmQiK38Y7m6PIU2e/owDyGN1zZAWae40S0oubZiTS+6NDEZFuk1G9zEjhPo883ONf2sYNb5S3A5km1UxIc9VGjm6UuxSmkI5dxKW49BcH/1VHZwk]]></Encrypt><AgentID><![CDATA[2000004]]></AgentID></xml>";

//        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(XML.toJSONObject(xml)));

//        JSONObject jsonObject = JSONUtil.parseObj(XML.toJSONObject(body));
//
//        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAESKey, corpid);
//
//        String sMsg = wxcpt.DecryptMsg(msgSignature, timestamp, nonce, body);
//        System.out.println("after decrypt msg: " + sMsg);


        JSONObject jsonObject = JSONUtil.parseFromXml(body);
        jsonObject = jsonObject.getJSONObject("xml");

        WXMsgCrypt crypt = new WXMsgCrypt(token, encodingAESKey, corpid);
        String s2 = crypt.decrypt(msgSignature, timestamp, nonce, jsonObject.getStr("Encrypt"));
        System.out.println(s2);

        System.out.println();
    }

}
