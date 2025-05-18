package com.tms.realtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.lang.reflect.Field;

/**
 * @author Felix
 * @date 2025/3/29
 */

@Data
@AllArgsConstructor
class Event{
    String mid;
    Integer ct;
    Long ts;
}
public class TestTrigger {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.socketTextStream("node1",8888).map(
            lineStr->{
                String[] fieldArr = lineStr.split(",");
                return new Event(fieldArr[0],Integer.parseInt(fieldArr[1]),Long.valueOf(fieldArr[2]));
            }
        ).assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<Event>forMonotonousTimestamps()
                .withTimestampAssigner(
                    new SerializableTimestampAssigner<Event>() {
                        @Override
                        public long extractTimestamp(Event element, long recordTimestamp) {
                            return element.getTs();
                        }
                    }
                )
        ).windowAll(
            TumblingEventTimeWindows.of(Time.days(1L))
        ).trigger(
            new MyTriggerFunctionTTT<Event>()
        ).aggregate(
            new AggregateFunction<Event, Event, Event>() {
                @Override
                public Event createAccumulator() {
                    return null;
                }

                @Override
                public Event add(Event value, Event accumulator) {
                    System.out.println("#####");
                    if (accumulator == null) {
                        return value;
                    }
                    accumulator.setCt(accumulator.getCt() + value.getCt());
                    return accumulator;
                }

                @Override
                public Event getResult(Event accumulator) {
                    return accumulator;
                }

                @Override
                public Event merge(Event a, Event b) {
                    return null;
                }
            },
            new ProcessAllWindowFunction<Event, Event, TimeWindow>() {
                @Override
                public void process(Context context, Iterable<Event> elements, Collector<Event> out) throws Exception {
                    System.out.println("1111");
                    for (Event element : elements) {
                        out.collect(element);
                    }
                }
            }
        ).print(">>>>");
        env.execute();
    }
}

/*
class MyTriggerFunctionTTT<T>  extends Trigger<T, TimeWindow> {
    @Override
    public TriggerResult onElement(T element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
        ValueStateDescriptor<Boolean> valueStateDescriptor
            = new ValueStateDescriptor<Boolean>("isFirstState",Boolean.class);
        ValueState<Boolean> isFirstState = ctx.getPartitionedState(valueStateDescriptor);
        Boolean isFirst = isFirstState.value();
        if(isFirst == null){
            //如果是窗口中的第一个元素   注册定时器，将事件时间向下取整后，注册10s后执行的定时器
            isFirstState.update(true);
            long nextTime = timestamp - timestamp % 10000L + 10000L;
            ctx.registerEventTimeTimer(nextTime);
        }else  if(isFirst){
            isFirstState.update(false);
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        long end = window.getEnd();
        if(time < end){
            if(time + 10000L < end){
                ctx.registerEventTimeTimer(time + 10000L);
            }
            return TriggerResult.FIRE;

        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
        ctx.deleteEventTimeTimer(window.maxTimestamp());
    }
}*/
class MyTriggerFunctionTTT<T> extends Trigger<T, TimeWindow> {
    @Override
    public TriggerResult onElement(T element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
        ValueState<Boolean> isFirstState = ctx.getPartitionedState(
            new ValueStateDescriptor<Boolean>("is-first-value", Boolean.class));
        Boolean isFirst = isFirstState.value();
        if (isFirst == null) {
            isFirstState.update(true);
            Field tsField = element.getClass().getDeclaredField("ts");
            tsField.setAccessible(true);
            Long ts = (Long)tsField.get(element);
            long nextTime = ts + 10 * 1000L - ts % (10 * 1000L);
            ctx.registerEventTimeTimer(nextTime);
        } else if (isFirst) {
            isFirstState.update(false);
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
        long edt = window.getEnd();
        if (time < edt) {
            if (time + 10 * 1000L < edt) {
                ctx.registerEventTimeTimer(time + 10 * 1000L);
            }
            return TriggerResult.FIRE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) {
    }
}