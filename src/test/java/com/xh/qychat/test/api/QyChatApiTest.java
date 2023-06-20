package com.xh.qychat.test.api;

import com.tencent.wework.Finance;
import com.xh.qychat.infrastructure.integration.qychat.adapter.QyChatAdapter;
import com.xh.qychat.infrastructure.integration.qychat.model.ChatRoomModel;
import com.xh.qychat.infrastructure.integration.qychat.properties.ChatDataProperties;
import com.xh.qychat.infrastructure.util.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Base64;

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
    public void decryptData() throws Exception {
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFCbR81MJbtPHSA3GwZC7dl7kAqn32AyRGHYHLNLkk0rAa25MaNg3FE4V2rtn9vkQEcpPUWpfCCX2PWe0Y7dePEE64r+qmkd6kaz1JHWtiQU0pT8ZevnY1/VRoigS5k6fGwSAWqD2jbbAdL7Jxl6cRGAYMYkDKE1GdZDA+ViCvdYpRKVsWI5d2HScISw7/u14FsHLo1Q/+hggSsRX+KP3Mm4g90n92fNxJJBdtTVUqyEEj1p9lu+BhYBqSBA5FRPZq7UcYf9ThUsG6UvwoBEjKe5z/yPJQGbtew75/D6cEQ5i0Omyu9k3RbtYzXPBW1GD4RvPUlDm8FGlps7r71KCzAgMBAAECggEAOPx5v/kA0fiVlwCbq9OWEZxihisBrqYU3SXZiZopbmEDR1Xj0pfx2QeuJTLrtJnSF9eehBlUJS0ciBGcko+axLDXD5xnFneoV2SKSW3dKCbRrw75aW8LQsAjbm4kbPuXI/doz9u8H2umQzJBrQ+pZBSWzqotl4Gj9ZfLZIBPGujH48CQ0vv15onqb7KHUX/VYr7r0yNo3XSgS6oyvZHkjtA00MvvuVmeIxUxyrOi3JQxCin4ILXGzcGGPa/yoWUYq3oJSAZa2jMF3XGuYukW1tX0QDfeYsBD9qycvloylBiryc2AVB+4hhiixtdzPecxQQTIyAEHFyrUrO9GbLJTvQKBgQCG0eBtsGGAn5YvbVtQ+2Bzmq3kgVwQWvEzc3So1uia8sYyMyFU5AUzL6Ij5l9D2HVDN2D5pe59ZY6vAG+RJaPGFmJYczYUhHG4oydbckueeSWYfb0O1aHKnyxlv4Mejiz+MB4wY6y+FhfyKpvyNzTbn/SIvnM1WU3SzgALuYsUJwKBgQD8nc6xOURvqfxmh9rTvky6Ka1KakUmrgE3DXAHSyFFJcx+as1SYAHtOR77M60srpHFshr9id84HkyqtC5o24LpjPF/ocj5oZR7wZX8sLXmX4lbsOLn325Mpxd2tI07Nss9FvIbpDjq4DGB8E2Y1AseDvrl7wguFPGWRLRqWYCqlQKBgAUyzIOOPIQ4IxNbFR1PRA3RFglsj3818nz4Y7Qq7TckS04eOJfMXwL1QDxAIsY773GTDhMyObruEo1e5+5h1CnMLSiZ8Kjj3nqvF9Jn2tCWfUOe/Y434JAuiQi3VLf15Xg7pL5a6Ys+0NeOmwrA5DHfvg8TwN96vQx20vugwOMbAoGAYcZat/GocWfjz3OJ2Sbw36U9822KclwrEDndQJVtvSvSKKQB8H5EgKL5QNVRk9fFiDVViQiSI1Bm8DNpOyCrXOY2muXtueE2bWgSO4nR/ebAbYLbcE/kizqpNELR3zdoFq8I6nX3qcE4qEhwYFTCMNIvfku0aNGmaDkTc45M5LkCgYAbV1Rn/mKad8adFnptz7E44ntXwwVSuvgabT8Igg5tjmUx1P+Qzcwuh/PlfRUZP40ZusEivzljHgdnlpvADkO7Wd1VHTH8dgTA+TRdBljgT/Tgp3Prl7SNWtjDdr2GXo7+Gt6sMpachvRaLla7pja9CFwAale8haFVh3ZjX1hu7A==";

        String encryptRandomKey = "bRgL1p/SbCzLaKbxYenc/870zuB+PNsd1qr3viR6hrIFGkzjlfxRlCCLEMDpR6ayBJ/7QDVvY67Uzzqj39jA/n3gLG7qG61k/+flOk/8pHGf7Yf1UXb7i6GhV0VI2WZTIWaBUXIEdQSXVcy8/TTPigd7x33SinQF9ic1pAqUe59rY0WFv1tb4xKicauTqEpOgtYq0GkXWQfZGOQnJJOF6P33N4jbFm6hPqvx4W0+rpCiGSh9qZ79EpEGuaBAyTJFSIXThnJet4uZloJ13ajoKrSaeLnJJQB7WzgDvER2jPpk5ylfLFPbhCvDvC37SvRkis8OruKlQkV/5r/8iP2e2A==";

        byte[] decoderData = Base64.getDecoder().decode(encryptRandomKey);
        byte[] decrypt = RsaUtils.decrypt(decoderData, RsaUtils.getPrivateKey(qychatDataProperties.getPrivateKey()));
        System.out.println(new String(decrypt, "UTF-8"));
    }


    @Test
    public void getChatRoomDetail() {
        ChatRoomModel chatRoomDetail = qychatAdapter.getChatRoomDetail("wrgQjpQAAAhSGCNKPd_I3hJ5vFvGFogA");
        System.out.println();
    }

    @Test
    public void getMemberDetail() {
        qychatAdapter.getMemberDetail("LinKang");
    }

    @Test
    public void getCustomerDetail() {
        qychatAdapter.getCustomerDetail("wmgQjpQAAAdhRlUt4NoZl_t6aad3Kj6Q");
    }

    @Test
    public void getPersonnelDetail() {
        qychatAdapter.getPersonnelDetail("wmgQjpQAAAdhRlUt4NoZl_t6aad3Kj6Q");
    }

    public static void main(String[] args) {
        String corpid = "wx5823bf96d3bd56c7";
        String token = "QDG6eK";

        String encodingAESKey = "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C";


        String msgSignature = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
        String timestamp = "1409659589";
        String nonce = "263014780";
        String echostr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";


//        WXBizJsonMsgCrypt crypt = new WXBizJsonMsgCrypt(token, encodingAESKey, corpid);
////        String sha1 = SHA1.getSHA1(token, timestamp, nonce, echostr);
////        System.out.println(sha1);
//
//        String s1 = crypt.VerifyURL(msgSignature, timestamp, nonce, echostr);
//        System.out.println(s1);

        System.out.println("==========================================================================================");

//        String[] array = new String[]{token, timestamp, nonce, echostr};
//        StringBuffer sb = new StringBuffer();
//        // 字符串排序
//        Arrays.sort(array);
//        for (int i = 0; i < 4; i++) {
//            sb.append(array[i]);
//        }
//        String str = sb.toString();
//
//        System.out.println(DigestUtil.sha1Hex(str.getBytes()));

//        String s = Stream.of(token, timestamp, nonce, echostr).sorted().collect(Collectors.joining());
//        System.out.println(DigestUtil.sha1Hex(s.getBytes()));

//        WXMsgCrypt crypt1 = new WXMsgCrypt(token, encodingAESKey, corpid);
//        String s2 = crypt1.verifyURL(msgSignature, timestamp, nonce, echostr);
//        System.out.println(s2);
    }

}
