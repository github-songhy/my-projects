package com.tms.realtime.app.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.beans.TmsConfigDimBean;
import com.tms.realtime.utils.DateFormatUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.sql.*;
import java.util.*;

/**
 * @author Felix
 * @date 2025/3/26
 * 自定义类  完成对主流和广播流数据的处理
 */
public class MyBroadcastProcessFunction extends BroadcastProcessFunction<JSONObject,String,JSONObject> {

    private MapStateDescriptor<String, TmsConfigDimBean> mapStateDescriptor;

    private Map<String,TmsConfigDimBean> configMap = new HashMap<>();

    private String username;

    private String password;

    public MyBroadcastProcessFunction(MapStateDescriptor<String, TmsConfigDimBean> mapStateDescriptor,String[] args) {
        this.mapStateDescriptor = mapStateDescriptor;
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        this.username = parameterTool.get("mysql-username","root");
        this.password = parameterTool.get("mysql-password","123456");
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        //将配置表中的数据进行预加载
        //注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //建立连接
        String url = "jdbc:mysql://node1:3306/tms_config?useSSL=false&useUnicode=true" +
            "&user=" + username + "&password=" + password +
            "&charset=utf8&TimeZone=Asia/Shanghai";
        Connection conn = DriverManager.getConnection(url);

        //获取数据库操作对象
        PreparedStatement ps = conn.prepareStatement("select * from tms_config.tms_config_dim");

        //执行SQL语句
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        //处理结果集
        while(rs.next()){
            JSONObject jsonObj = new JSONObject();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = rs.getObject(i);
                jsonObj.put(columnName,columnValue);
            }
            TmsConfigDimBean tmsConfigDimBean = jsonObj.toJavaObject(TmsConfigDimBean.class);
            configMap.put(tmsConfigDimBean.getSourceTable(),tmsConfigDimBean);
        }
        //释放资源
        rs.close();
        ps.close();
        conn.close();
    }

    //处理主流业务数据
    @Override
    public void processElement(JSONObject jsonObj, ReadOnlyContext ctx, Collector<JSONObject> out) throws Exception {
        //获取操作的业务数据库表的表名
        String table = jsonObj.getString("table");
        //获取广播状态
        ReadOnlyBroadcastState<String, TmsConfigDimBean> broadcastState = ctx.getBroadcastState(mapStateDescriptor);
        //根据操作的业务数据库表的表名 到广播状态中获取对应的配置信息
        TmsConfigDimBean tmsConfigDimBean = null;

        if((tmsConfigDimBean = broadcastState.get(table))!=null || (tmsConfigDimBean = configMap.get(table))!=null){
            //如果对应的配置信息不为空   说明是维度数据
            
            //获取after对象，对应的是影响的业务数据库表中的一条记录
            JSONObject afterJsonObj = jsonObj.getJSONObject("after");
            
            // 数据脱敏
            switch (table) {
                // 员工表信息脱敏
                case "employee_info":
                    String empPassword = afterJsonObj.getString("password");
                    String empRealName = afterJsonObj.getString("real_name");
                    String idCard = afterJsonObj.getString("id_card");
                    String phone = afterJsonObj.getString("phone");

                    // 脱敏
                    empPassword = DigestUtils.md5Hex(empPassword);
                    empRealName = empRealName.charAt(0) +
                        empRealName.substring(1).replaceAll(".", "\\*");
                    //知道有这个操作  idCard是随机生成的，和标准的格式不一样 所以这里注释掉
                    // idCard = idCard.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)")
                    //     ? DigestUtils.md5Hex(idCard) : null;
                    phone = phone.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
                        ? DigestUtils.md5Hex(phone) : null;

                    afterJsonObj.put("password", empPassword);
                    afterJsonObj.put("real_name", empRealName);
                    afterJsonObj.put("id_card", idCard);
                    afterJsonObj.put("phone", phone);
                    break;
                // 快递员信息脱敏
                case "express_courier":
                    String workingPhone = afterJsonObj.getString("working_phone");
                    workingPhone = workingPhone.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
                        ? DigestUtils.md5Hex(workingPhone) : null;
                    afterJsonObj.put("working_phone", workingPhone);
                    break;
                // 卡车司机信息脱敏
                case "truck_driver":
                    String licenseNo = afterJsonObj.getString("license_no");
                    licenseNo = DigestUtils.md5Hex(licenseNo);
                    afterJsonObj.put("license_no", licenseNo);
                    break;
                // 卡车信息脱敏
                case "truck_info":
                    String truckNo = afterJsonObj.getString("truck_no");
                    String deviceGpsId = afterJsonObj.getString("device_gps_id");
                    String engineNo = afterJsonObj.getString("engine_no");

                    truckNo = DigestUtils.md5Hex(truckNo);
                    deviceGpsId = DigestUtils.md5Hex(deviceGpsId);
                    engineNo = DigestUtils.md5Hex(engineNo);

                    afterJsonObj.put("truck_no", truckNo);
                    afterJsonObj.put("device_gps_id", deviceGpsId);
                    afterJsonObj.put("engine_no", engineNo);
                    break;
                // 卡车型号信息脱敏
                case "truck_model":
                    String modelNo = afterJsonObj.getString("model_no");
                    modelNo = DigestUtils.md5Hex(modelNo);
                    afterJsonObj.put("model_no", modelNo);
                    break;
                // 用户地址信息脱敏
                case "user_address":
                    String addressPhone = afterJsonObj.getString("phone");
                    addressPhone = addressPhone.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
                        ? DigestUtils.md5Hex(addressPhone) : null;
                    afterJsonObj.put("phone", addressPhone);
                    break;
                // 用户信息脱敏
                case "user_info":
                    String passwd = afterJsonObj.getString("passwd");
                    String realName = afterJsonObj.getString("real_name");
                    String phoneNum = afterJsonObj.getString("phone_num");
                    String email = afterJsonObj.getString("email");

                    // 脱敏
                    passwd = DigestUtils.md5Hex(passwd);
                    if(StringUtils.isNotEmpty(realName)){
                        realName = DigestUtils.md5Hex(realName);
                        afterJsonObj.put("real_name", realName);
                    }
                    phoneNum = phoneNum.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
                        ? DigestUtils.md5Hex(phoneNum) : null;
                    email = email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
                        ? DigestUtils.md5Hex(email) : null;

                    afterJsonObj.put("birthday", DateFormatUtil.toDate(afterJsonObj.getInteger("birthday") * 24 * 60 * 60 * 1000L));
                    afterJsonObj.put("passwd", passwd);
                    afterJsonObj.put("phone_num", phoneNum);
                    afterJsonObj.put("email", email);
                    break;
            }
            
            //过滤不需要传递的维度属性
            String sinkColumns = tmsConfigDimBean.getSinkColumns();
            filterColumn(afterJsonObj,sinkColumns);

            //补充输出的目的地表名
            String sinkTable = tmsConfigDimBean.getSinkTable();
            afterJsonObj.put("sink_table",sinkTable);

            //补充rowKey字段
            String sinkPk = tmsConfigDimBean.getSinkPk();
            afterJsonObj.put("sink_pk",sinkPk);

            //清除Redis缓存的准备工作(传递操作类型----传递可以用于查询当前维度表的外键字段名以及对应的值)
            String op = jsonObj.getString("op");
            if("u".equals(op)){
                //将操作类型传递到下游
                afterJsonObj.put("op",op);

                //从配置表中获取当前维度表关联的外键名
                String foreignKeys = tmsConfigDimBean.getForeignKeys();
                //定义一个json对象，用于存储当前维度表对应的外键名以及外键值
                JSONObject foreignJsonObj = new JSONObject();
                if(StringUtils.isNotEmpty(foreignKeys)){
                    String[] foreignNameArr = foreignKeys.split(",");
                    for (String foreignName : foreignNameArr) {
                        //获取修改前的数据
                        JSONObject before = jsonObj.getJSONObject("before");
                        //获取外键修改前的值
                        String foreignKeyBefore = before.getString(foreignName);
                        //获取外键修改后的值
                        String foreignKeyAfter = afterJsonObj.getString(foreignName);
                        if(!foreignKeyBefore.equals(foreignKeyAfter)){
                            //如果修改的是外键
                            foreignJsonObj.put(foreignName,foreignKeyBefore);
                        }else{
                            foreignJsonObj.put(foreignName,foreignKeyAfter);
                        }
                    }
                }
                afterJsonObj.put("foreign_key",foreignJsonObj);
            }

            //将维度数据传递到下游
            out.collect(afterJsonObj);
        }
    }


    private void filterColumn(JSONObject afterJsonObj, String sinkColumns) {
        String[] fieldArr = sinkColumns.split(",");
        List<String> fieldList = Arrays.asList(fieldArr);
        Set<Map.Entry<String, Object>> entrySet = afterJsonObj.entrySet();
        entrySet.removeIf(entry->!fieldList.contains(entry.getKey()));
    }

    //处理广播流中数据
    @Override
    public void processBroadcastElement(String jsonStr, Context ctx, Collector<JSONObject> out) throws Exception {
        //"op":"r": {"before":null,"after":{"source_table":"base_dic","sink_table":"dim_base_dic","sink_family":null,"sink_columns":"id,name","sink_pk":"id"},"source":{"version":"1.6.4.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":0,"snapshot":"false","db":"tms_config","sequence":null,"table":"tms_config_dim","server_id":0,"gtid":null,"file":"","pos":0,"row":0,"thread":null,"query":null},"op":"r","ts_ms":1685097006581,"transaction":null}
        //"op":"c": {"before":null,"after":{"source_table":"a","sink_table":"aaa","sink_family":null,"sink_columns":"id,name","sink_pk":"id"},"source":{"version":"1.6.4.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":1685097108000,"snapshot":"false","db":"tms_config","sequence":null,"table":"tms_config_dim","server_id":1,"gtid":null,"file":"binlog.000016","pos":5443,"row":0,"thread":null,"query":null},"op":"c","ts_ms":1685097107833,"transaction":null}
        //"op":"u": {"before":{"source_table":"a","sink_table":"aaa","sink_family":null,"sink_columns":"id,name","sink_pk":"id"},"after":{"source_table":"a","sink_table":"aaabbb","sink_family":null,"sink_columns":"id,name","sink_pk":"id"},"source":{"version":"1.6.4.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":1685097157000,"snapshot":"false","db":"tms_config","sequence":null,"table":"tms_config_dim","server_id":1,"gtid":null,"file":"binlog.000016","pos":5780,"row":0,"thread":null,"query":null},"op":"u","ts_ms":1685097156984,"transaction":null}
        //"op":"d":{"before":{"source_table":"a","sink_table":"aaabbb","sink_family":null,"sink_columns":"id,name","sink_pk":"id"},"after":null,"source":{"version":"1.6.4.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":1685097212000,"snapshot":"false","db":"tms_config","sequence":null,"table":"tms_config_dim","server_id":1,"gtid":null,"file":"binlog.000016","pos":6134,"row":0,"thread":null,"query":null},"op":"d","ts_ms":1685097211485,"transaction":null}
        // System.out.println(jsonStr);
        //为了处理方便  将jsonStr转换为jsonObj
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        //获取广播状态
        BroadcastState<String, TmsConfigDimBean> broadcastState = ctx.getBroadcastState(mapStateDescriptor);
        //获取对配置表的操作类型
        String op = jsonObj.getString("op");
        if("d".equals(op)){
            //如果从配置表中删除了一条数据  从广播状态将对应的配置信息删掉
            String sourceTable = jsonObj.getJSONObject("before").getString("source_table");
            broadcastState.remove(sourceTable);
            configMap.remove(sourceTable);
        }else{
            //如果对配置表中做了删除外的其它操作  将对应的配置信息放到或者更新到广播状态
            TmsConfigDimBean configDimBean = jsonObj.getObject("after", TmsConfigDimBean.class);
            //获取维度表表名
            String sourceTable = configDimBean.getSourceTable();
            broadcastState.put(sourceTable,configDimBean);
            configMap.put(sourceTable,configDimBean);
        }

    }
}
