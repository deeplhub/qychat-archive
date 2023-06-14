package com.xh.qychat.infrastructure.strategy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Title: 自定义注射策略
 * Description:
 *
 * @author H.Yang
 * @date 2020/11/24
 */
@Component
@Slf4j
public class ConvertorStrategy {

    public BigDecimal stringToBigDecimal(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return Convert.toBigDecimal(str);
    }

    public String bigDecimalToString(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }
        return Convert.toStr(bigDecimal);
    }

    public Double stringToDouble(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return Convert.toDouble(str);
    }

    public Long stringToLong(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return Convert.toLong(str);
    }

    public String longToString(Long val) {
        if (val == null) {
            return null;
        }
        return Convert.toStr(val);
    }

    public Date stringToDate(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return Convert.toDate(str);
    }

    public String dateTimeToString(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.formatDateTime(date);
    }

    public Integer stringToInt(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return Convert.toInt(str);
    }

}
