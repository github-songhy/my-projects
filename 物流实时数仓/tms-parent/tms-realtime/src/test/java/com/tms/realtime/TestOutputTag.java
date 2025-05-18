package com.tms.realtime;

import org.apache.flink.util.OutputTag;

/**
 * @author Felix
 * @date 2025/3/27
 */
public class TestOutputTag {
    public static void main(String[] args) {
        OutputTag<String> testTag = new OutputTag<String>("test"){};
        // OutputTag<String> testTag = new OutputTag<String>("test");
    }
}
