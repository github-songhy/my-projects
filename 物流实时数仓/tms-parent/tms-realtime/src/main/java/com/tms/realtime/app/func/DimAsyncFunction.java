package com.tms.realtime.app.func;

import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.beans.DimJoinFunction;
import com.tms.realtime.common.TmsConfig;
import com.tms.realtime.utils.DimUtil;
import com.tms.realtime.utils.ThreadPoolUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

/**
 * @author Felix
 * @date 2025/3/30
 * 发送异步请求进行维度关联
 * 模板方法设计模式：
 *      在父类中定义完成某一个功能的核心算法的骨架(步骤)，有些步骤具体的实现延迟到子类中去完成。
 *      在不改变父类核心算法骨架的前提下，每一个子类都可以有自己不同的实现。
 */
public abstract class DimAsyncFunction<T> extends RichAsyncFunction<T,T> implements DimJoinFunction<T> {
    private String tableName;

    private ExecutorService executorService;

    public DimAsyncFunction(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        // 初始化线程池对象
        executorService = ThreadPoolUtil.getInstance();
    }

    @Override
    public void asyncInvoke(T obj, ResultFuture<T> resultFuture) throws Exception {
        //1.从线程池中获取线程，发送异步请求
        executorService.submit(
            new Runnable() {
                @Override
                public void run() {
                    //2.根据流中的对象获取要作为查询条件的主键或者外键
                    Tuple2<String,String> keyNameAndValue = getCondition(obj);
                    //3.根据查询条件获取维度对象
                    JSONObject dimInfoJsonObj = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, tableName, keyNameAndValue);
                    //4.将维度对象的属性补充到流中的对象上
                    if(dimInfoJsonObj != null){
                        join(obj,dimInfoJsonObj);
                    }
                    //5.向下游传递数据
                    resultFuture.complete(Collections.singleton(obj));
                }
            }
        );
    }


}
