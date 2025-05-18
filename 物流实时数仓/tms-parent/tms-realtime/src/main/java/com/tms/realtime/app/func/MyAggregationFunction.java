package com.tms.realtime.app.func;

import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * @author Felix
 * @date 2025/3/29
 * 聚合逻辑实现类
 */
public abstract class MyAggregationFunction<T> implements AggregateFunction<T,T,T> {
    @Override
    public T createAccumulator() {
        return null;
    }

    @Override
    public T getResult(T accumulator) {
        return accumulator;
    }

    @Override
    public T merge(T a, T b) {
        return null;
    }

    //静态方法使用泛型模板要在static后声明 如<U>，即使是使用类已经声明过的 <T> 也要在static后声明
    public static <U> void test(){
       U  u = null;
    }
}
