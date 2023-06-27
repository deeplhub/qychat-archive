package com.xh.qychat.infrastructure.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义填充公共字段
 * <p>
 * 保存或更新数据时自定义填充公共字段
 * <p>
 * 参考地址：
 * https://baomidou.com/guide/auto-fill-metainfo.html
 *
 * @author H.Yang
 * @date 2020/9/27
 */
@Slf4j
@Configuration
public class DateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
// 获取到需要被填充的字段的值
        Object createTime = this.getFieldValByName("createTime", metaObject);
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (ObjectUtils.isEmpty(createTime) || ObjectUtils.isEmpty(updateTime)) {
            if (ObjectUtils.isEmpty(createTime)) {
                this.setFieldValByName("createTime", DateUtil.date(), metaObject);
            }
            if (ObjectUtils.isEmpty(updateTime)) {
                this.setFieldValByName("updateTime", DateUtil.date(), metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateUtil.date(), metaObject);
    }
}

