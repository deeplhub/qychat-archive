package com.xh.qychat.infrastructure.common.enums;

/**
 * 响应枚举类
 *
 * @Author H.Yang
 */
public enum ResponseEnum implements BaseEnum<Integer, String, ResponseEnum> {
    /************************************************************************
     *
     * 0 请求成功
     *
     ***********************************************************************/
    SUCCESS(0, "成功"),


    /************************************************************************
     *
     * 4xx 客户端异常
     *
     ***********************************************************************/
    BODY_NOT_MATCH(400, "参数解析失败"),
    //    SIGNATURE_NOT_MATCH(401, "数字签名不匹配"),
//    NOT_LOGIN(401, "用户未登录"),
    REQUEST_PARAMETERS(402, "参数不存在或参数有误"),
    PERMISSION_DENIED(403, "无权访问"),
    PERMISSION_TOKEN_DENIED(403, "无权访问：token不存在"),
    RESOURCES_NOT_EXIST(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持当前媒体类型"),
    DATA_ALREADY_EXISTS(406, "数据已存在"),
//    PARAMETERS_NOT_NULL(407, "参数不能为空"),
//    FREQUENT_REQUESTS(408, "请求过于频繁"),


    /************************************************************************
     *
     * 5xx 服务端异常
     *
     ***********************************************************************/
    INTERNAL_SERVER_FAIL(500, "服务器内部错误"),
    //    FEIGN_FAILED(500, "Feign调用错误"),
//    SERVER_BUSY(503, "服务器正忙，请稍后再试"),
    INVOKE_SQL_FAIL(500, "SQL语法异常"),
    //    DATA_NON_EXISTENT(505, "数据不存在"),
//    DATA_ALREADY_EXISTED(506, "数据已存在"),
//    VERIFY_SIGNATURE_FAIL(507, "签名验证失败"),
//    PERMISSION_DENIED_NOTIN_WHITE(511, "无权访问:白名单未配置"),
//    PERMISSION_DENIED_IN_BLACK(512, "无权访问:黑名单已配置"),
    DATABASE_FAIL(513, "数据库异常"),
    REDIS_FAIL(514, "Redis异常"),


    /************************************************************************
     *
     * 6xx RPC调用异常
     *
     ***********************************************************************/
    FREQUENT_FLOW(601, "接口降级了"),
    FREQUENT_DEGRADE(602, "接口限流了"),
    FREQUENT_PARAM_FLOW(603, "接口热点参数限流了"),
    FREQUENT_SYSTEM_BLOCK(604, "接口触发系统保护规则了"),
    FREQUENT_AUTHORITY(605, "当前接口授权不通过"),


    /************************************************************************
     *
     * 9xx 业务异常
     *
     ***********************************************************************/
    BUSINESS_FAIL(999, "业务错误");

    private final Integer code;
    private final String note;

    ResponseEnum(Integer code, String note) {
        this.code = code;
        this.note = note;
    }

    @Override
    public ResponseEnum get() {
        return this;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getNote() {
        return note;
    }
}
