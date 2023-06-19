package com.xh.qychat.infrastructure.common.enums;

/**
 * 公共枚举接口
 *
 * @author H.Yang
 * @date 2021/8/11
 */
public interface BaseEnum<K, V, C extends Enum> {

    /**
     * 返回枚举对象
     */
    C get();


    /**
     * 获取枚举编码
     *
     * @return
     */
    K getCode();

    /**
     * 获取枚举描述
     *
     * @return
     */
    V getNote();
}
