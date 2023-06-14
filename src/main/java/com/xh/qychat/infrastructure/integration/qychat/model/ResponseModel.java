package com.xh.qychat.infrastructure.integration.qychat.model;

import lombok.Data;

/**
 * @author H.Yang
 * @date 2022/1/19
 */
@Data
public class ResponseModel {

    /**
     * 返回码
     */
    private Integer errcode;

    /**
     * 对返回码的文本描述内容
     */
    private String errmsg;
}
