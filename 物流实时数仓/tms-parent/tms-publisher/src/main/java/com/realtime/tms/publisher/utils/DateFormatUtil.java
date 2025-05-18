package com.realtime.tms.publisher.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author Felix
 * @date 2025/3/3
 * 日期转换工具类
 */
public class DateFormatUtil {
    public static Integer now(){
        String yyyyMMdd = DateFormatUtils.format(new Date(), "yyyyMMdd");
        return Integer.valueOf(yyyyMMdd);
    }
}
