package com.xh.qychat.infrastructure.integration.qychat.constants;

/**
 * @author H.Yang
 * @date 2023/6/14
 */
public interface QychatConstants {

    /**
     * 一次拉取的消息条数，最大值1000条，超过1000条会返回错误
     */
    Integer MSG_MAX_LIMIT = 1000;

    /**
     * 最大文件
     */
    Long MAX_FILE_SIZE = 20 * 1024 * 1024L;

    String QYCHAT_TOKEN_KEY = "QYCHAT_TOKEN";

    /**
     * 获取accessToken
     */
    String TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    /**
     * 获取客户群详情
     */
    String GROUP_CHAT_GET_URL = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/get?access_token=";

    /**
     * 获取客户群列表
     */
    String GROUP_CHAT_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/list";


    String OUTSIDER_URL = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get";

    String INSIDER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get";
}
