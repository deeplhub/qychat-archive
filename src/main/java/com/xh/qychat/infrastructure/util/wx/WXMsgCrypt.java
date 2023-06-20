package com.xh.qychat.infrastructure.util.wx;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author H.Yang
 * @date 2023/6/16
 */
public class WXMsgCrypt {

    private final String token, receiveId;
    private final byte[] aesKey;

    /**
     * @param token          企业微信后台，开发者设置的token
     * @param encodingAesKey 企业微信后台，开发者设置的EncodingAESKey
     * @param receiveId      不同场景含义不同，详见文档
     */
    public WXMsgCrypt(String token, String encodingAesKey, String receiveId) {
        if (encodingAesKey.length() != 43) throw new RuntimeException("不合法的媒体文件类型");

        this.token = token;
        this.receiveId = receiveId;
        this.aesKey = Base64.decode(encodingAesKey + "=");
    }

    /**
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timestamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param echostr      随机串，对应URL参数的echostr
     * @return 解密之后的echostr
     */
    public String verifyURL(String msgSignature, String timestamp, String nonce, String echostr) {
        String signature = this.sha1Hex(timestamp, nonce, echostr);

        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("不合法的secret参数");
        }
        return this.decrypt(echostr);
    }

    private String sha1Hex(String timestamp, String nonce, String echostr) {
        String signature = Stream.of(token, timestamp, nonce, echostr).sorted().collect(Collectors.joining());
        return DigestUtil.sha1Hex(signature.getBytes());
    }

    /**
     * 对密文进行解密
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     */
    private String decrypt(String text) {
        SymmetricCrypto crypto = new SymmetricCrypto("AES/CBC/NoPadding", new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16)));
        // 使用BASE64对密文进行解码
        byte[] encrypted = Base64.decode(text);

        // 解密
        byte[] original = crypto.decrypt(encrypted);

        String jsonContent, fromReceiveid;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7EncoderUtils.decode(original);

            // 分离16位随机字符串,网络字节序和receiveid
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int jsonLength = recoverNetworkBytesOrder(networkOrder);

            jsonContent = new String(Arrays.copyOfRange(bytes, 20, 20 + jsonLength), StandardCharsets.UTF_8);
            fromReceiveid = new String(Arrays.copyOfRange(bytes, 20 + jsonLength, bytes.length), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("不合法的msgtype参数");
        }

        // receiveid不相同的情况
        if (!fromReceiveid.equals(receiveId)) {
            throw new RuntimeException("不合法的type参数");
        }
        return jsonContent;
    }

    // 还原4个字节的网络字节序
    private int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }


    /**
     * 检验消息的真实性，并且获取解密后的明文.
     * <ol>
     * 	<li>利用收到的密文生成安全签名，进行签名验证</li>
     * 	<li>若验证通过，则提取json中的加密消息</li>
     * 	<li>对消息进行解密</li>
     * </ol>
     *
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timestamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param encrypt      加密的消息
     * @return 解密后的原文
     */
    public String decrypt(String msgSignature, String timestamp, String nonce, String encrypt) {
        String signature = this.sha1Hex(timestamp, nonce, encrypt);

        // 和URL中的签名比较是否相等
        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("不合法的secret参数");
        }

        // 解密
        return this.decrypt(encrypt);
    }

}
