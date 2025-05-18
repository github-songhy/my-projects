package com.tms.realtime.utils;

import com.tms.realtime.beans.TransientSink;
import com.tms.realtime.common.TmsConfig;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Felix
 * @date 2025/3/31
 * 操作Clickhouse的工具类
 */
public class ClickHouseUtil {
    //获取SinkFunction
    public static <T>SinkFunction<T> getJdbcSink(String sql){
        SinkFunction<T> sinkFunction = JdbcSink.<T>sink(
            // "insert into dws_bound_org_sort_day_base values(?,?,?,?,?,?,?,?,?)"
            sql,
            new JdbcStatementBuilder<T>() {
                @Override
                public void accept(PreparedStatement ps, T obj) throws SQLException {
                    //将流中对象的属性  给问号占位符赋值
                    //获取当前流中对象所属的类型  以及类中的属性
                    Field[] fieldArr = obj.getClass().getDeclaredFields();
                    //遍历所有属性
                    int skipNum = 0;
                    for (int i = 0; i < fieldArr.length; i++) {
                        Field field = fieldArr[i];
                        //判断当前属性是否需要向CK中保存
                        TransientSink transientSink = field.getAnnotation(TransientSink.class);
                        if(transientSink != null){
                            skipNum++;
                            continue;
                        }

                        //设置私有属性的访问权限
                        field.setAccessible(true);
                        //获取属性的值
                        try {
                            Object fieldValue = field.get(obj);
                            ps.setObject(i + 1 - skipNum ,fieldValue);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },
            new JdbcExecutionOptions.Builder()
                .withBatchSize(5000)
                .withBatchIntervalMs(3000L)
                .build(),
            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withDriverName(TmsConfig.CLICKHOUSE_DRIVER)
                .withUrl(TmsConfig.CLICKHOUSE_URL)
                .build()
        );
        return sinkFunction;
    }
}
