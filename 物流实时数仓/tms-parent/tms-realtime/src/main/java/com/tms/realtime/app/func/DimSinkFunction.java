package com.tms.realtime.app.func;

import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.common.TmsConfig;
import com.tms.realtime.utils.DimUtil;
import com.tms.realtime.utils.HbaseUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.hadoop.hbase.client.Put;

import java.util.Map;
import java.util.Set;

/**
 * @author Felix
 * @date 2025/3/26
 * 将维度流中的数据写到Hbase
 */
public class DimSinkFunction implements SinkFunction<JSONObject> {

    @Override
    public void invoke(JSONObject jsonObj, Context context) throws Exception {
        // {"name":"已取件","sink_table":"dim_base_dic","id":60030,"sink_pk":"id"}
        //获取输出的目的地表名
        String sinkTable = jsonObj.getString("sink_table");
        jsonObj.remove("sink_table");
        //获取主键字段  作为写到hbase表中的rowkey
        String sinkPk = jsonObj.getString("sink_pk");
        jsonObj.remove("sink_pk");

        String op = jsonObj.getString("op");
        jsonObj.remove("op");

        JSONObject foreignKeyJsonObj = jsonObj.getJSONObject("foreign_key");
        jsonObj.remove("foreign_key");


        // {"name":"已取件","id":60030}
        //获取json对象中的每一个键值对
        Set<Map.Entry<String, Object>> entrySet = jsonObj.entrySet();
        Put put = new Put(jsonObj.getString(sinkPk).getBytes());
        //遍历出json中的每一对元素
        for (Map.Entry<String, Object> entry : entrySet) {
            if(!sinkPk.equals(entry.getKey())){
                put.addColumn("info".getBytes(),entry.getKey().getBytes(),entry.getValue().toString().getBytes());
            }
        }
        System.out.println("向hbase表中插入数据");
        HbaseUtil.putRow(TmsConfig.HBASE_NAMESPACE,sinkTable,put);

        //如果维度数据发生了变化，将Redis中缓存的维度数据清空掉
        if("u".equals(op)){
            //删除当前维度数据在Redis中对应的主键缓存
            DimUtil.delCached(sinkTable, Tuple2.of("id",jsonObj.getString("id")));
            //删除当前维度数据在Redis中对应的外键缓存
            Set<Map.Entry<String, Object>> set = foreignKeyJsonObj.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                DimUtil.delCached(sinkTable, Tuple2.of(entry.getKey(),entry.getValue().toString()));
            }
        }
    }
}
