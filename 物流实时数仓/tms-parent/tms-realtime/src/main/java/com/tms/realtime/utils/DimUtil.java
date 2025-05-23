package com.tms.realtime.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.common.TmsConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import redis.clients.jedis.Jedis;

/**
 * @author Felix
 * @date 2025/3/30
 * 查询维度的工具类
 * 优化：旁路缓存
 * 思路：
 *      先从缓存中查询维度数据，如果在缓存中能够找到要查询的维度，那么直接将其作为方法的返回值进行返回(缓存命中)；
 *      如果在缓存中，没有找到要查询的维度数据，发送请求到hbase中去查找维度，并将查询出来的维度数据放到缓存中进行缓存，方便下次查询使用。
 * 缓存产品的选型：
 *      状态：     性能好，维护性差
 *      Redis：   性能也不错，维护性好   √
 * Redis相关设置
 *      key:   dim:维度表表名:查询条件的字段名_字段值
 *      type:   String
 *      TTL:    1day    避免冷数据常驻内容，给内存带来压力
 *      注意：如果hbase中的维度表数据发生了变化，需要将缓存的维度数据清除掉
 */
public class DimUtil {
    public static JSONObject getDimInfo(String namespace, String tableName, Tuple2<String,String> nameAndValue){
        //获取的查询条件中字段名以及字段值
        String keyName = nameAndValue.f0;
        String keyValue = nameAndValue.f1;
        //拼接从Redis中查询数据的key
        String redisKey = "dim:"+tableName.toLowerCase()+":"+keyName+"_"+keyValue;

        //操作Redis的客户端
        Jedis jedis = null;
        //用于存放从Redis中查询出来的维度
        String dimJsonStr = null;
        //用于封装返回结果
        JSONObject dimJsonObj = null;

        try{
            // 先从缓存中查询维度数据
            jedis = JedisUtil.getJedis();
            dimJsonStr = jedis.get(redisKey);
            if(StringUtils.isNotEmpty(dimJsonStr)){
                // 如果在缓存中能够找到要查询的维度，那么直接将其作为方法的返回值进行返回(缓存命中)
                dimJsonObj = JSON.parseObject(dimJsonStr);
            }else{
                // 如果在缓存中，没有找到要查询的维度数据，发送请求到hbase中去查找维度
                if("id".equals(keyName)){
                    dimJsonObj = HbaseUtil.getRowByPrimaryKey(namespace,tableName,nameAndValue);
                }else{
                    dimJsonObj = HbaseUtil.getRowByForeignKey(namespace,tableName,nameAndValue);
                }
                if(dimJsonObj != null && jedis != null){
                    // 并将查询出来的维度数据放到缓存中进行缓存，方便下次查询使用
                    jedis.setex(redisKey,3600*24,dimJsonObj.toJSONString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("从Redis中查询维度数据发生了异常");
        }finally {
            if(jedis != null){
//                System.out.println("~~~关闭Jedis客户端~~~");
                jedis.close();
            }
        }
        return dimJsonObj;
    }

    //从Redis中删除缓存的维度数据
    public static void delCached(String tableName,Tuple2<String,String> keyNameAndValue){
        String keyName = keyNameAndValue.f0;
        String keyValue = keyNameAndValue.f1;
        String redisKey = "dim:"+tableName.toLowerCase()+":"+keyName+"_"+keyValue;
        Jedis jedis = null;
        try {
            jedis = JedisUtil.getJedis();
            jedis.del(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("清除Redis中缓存的维度数据时发生了异常");
        } finally {
            if(jedis != null){
                System.out.println("~~~清除缓存后，关闭Jedis客户端~~~");
                jedis.close();
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_user_info", Tuple2.of("id","99")));
        // System.out.println(getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_organ", Tuple2.of("region_id","110102")));
    }
}
