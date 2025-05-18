package com.tms.realtime.utils;

import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.common.TmsConfig;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @author Felix
 * @date 2025/3/24
 * 操作Hbase的工具类
 */
public class HbaseUtil {
    private static Connection conn;
    static {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", TmsConfig.HBASE_ZOOKEEPER_QUORUM);
            conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //创建表
    public static void createTable(String nameSpace,String tableName,String ... families){

        Admin admin = null;
        try {
            if (families.length < 1) {
                System.out.println("至少需要一个列族");
                return;
            }
            admin = conn.getAdmin();
            //判断表是否存在
            if(admin.tableExists(TableName.valueOf(nameSpace,tableName))){
                System.out.println(nameSpace + ":" + tableName + "已存在");
                return;
            }
            TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(TableName.valueOf(nameSpace, tableName));
            //指定列族
            for (String family : families) {
                ColumnFamilyDescriptor familyDescriptor = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes(family)).build();
                builder.setColumnFamily(familyDescriptor);
            }
            admin.createTable(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(admin != null){
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //向hbase表中插入一行数据
    public static void putRow(String namespace,String tableName,Put put){
        BufferedMutator mutator = null;
        try {
            // mutator = conn.getBufferedMutator(TableName.valueOf(namespace, tableName));
            BufferedMutatorParams params
                = new BufferedMutatorParams(TableName.valueOf(namespace, tableName));
            params.writeBufferSize(5*1024*1024);
            params.setWriteBufferPeriodicFlushTimeoutMs(3000L);
            mutator = conn.getBufferedMutator(params);
            mutator.mutate(put);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(mutator != null){
                try {
                    mutator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //根据主键从hbase表中查询一行数据
    public static JSONObject getRowByPrimaryKey(String namespace, String tableName, Tuple2<String,String> rowKeyNameAndKey){
        Table table = null;
        JSONObject dimJsonObj = null;
        String rowKeyName = rowKeyNameAndKey.f0;
        String rowKeyValue = rowKeyNameAndKey.f1;
        try {
            table = conn.getTable(TableName.valueOf(namespace,tableName));
            Get get = new Get(Bytes.toBytes(rowKeyValue));
            Result result = table.get(get);
            Cell[] cells = result.rawCells();
            if(cells.length > 0){
                dimJsonObj = new JSONObject();
                dimJsonObj.put(rowKeyName,rowKeyValue);
                for (Cell cell : cells) {
                    dimJsonObj.put(Bytes.toString(CellUtil.cloneQualifier(cell)),Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }else{
                System.out.println("从hbase表中没有找到对应的维度数据");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dimJsonObj;
    }

    // 根据外键从hbase表中查询一行数据
    public static JSONObject getRowByForeignKey(String namespace, String tableName, Tuple2<String,String> foreignKeyNameAndKey){
        Table table = null;
        JSONObject dimJsonObj = null;
        try {
            table = conn.getTable(TableName.valueOf(namespace, tableName));
            Scan scan = new Scan();
            String foreignKeyName = foreignKeyNameAndKey.f0;
            String foreignKeyValue = foreignKeyNameAndKey.f1;
            SingleColumnValueFilter singleColumnValueFilter
                = new SingleColumnValueFilter(Bytes.toBytes("info"),Bytes.toBytes(foreignKeyName), CompareOperator.EQUAL,Bytes.toBytes(foreignKeyValue));

            singleColumnValueFilter.setFilterIfMissing(true);
            scan.setFilter(singleColumnValueFilter);

            ResultScanner scanner = table.getScanner(scan);
            Result result = scanner.next();
            if(result != null){
                Cell[] cells = result.rawCells();
                if(cells.length > 0){
                    dimJsonObj = new JSONObject();
                    dimJsonObj.put("id",Bytes.toString(result.getRow()));
                    for (Cell cell : cells) {
                        dimJsonObj.put(Bytes.toString(CellUtil.cloneQualifier(cell)),Bytes.toString(CellUtil.cloneValue(cell)));
                    }
                }
            }else{
                System.out.println("从hbase表中没有找到对应的维度数据");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dimJsonObj;
    }

    public static void main(String[] args) {
        // System.out.println(getRowByPrimaryKey(TmsConfig.HBASE_NAMESPACE, "dim_user_info", Tuple2.of("id","99")));
        System.out.println(getRowByForeignKey(TmsConfig.HBASE_NAMESPACE, "dim_base_organ", Tuple2.of("region_id","110102")));
    }
}
