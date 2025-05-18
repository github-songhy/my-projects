package com.tms.realtime.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Felix
 * @date 2025/3/30
 * 获取线程池工具类
 *      通过双重校验锁解决单例设计模式懒汉式线程安全问题
 */
public class ThreadPoolUtil {

    //当volatile变量被修改时，新值会立即刷新到主内存；
    //其他线程读取该变量时，会强制从主内存重新加载最新值，而不是使用线程本地缓存
    private static volatile ThreadPoolExecutor poolExecutor;

    public static ThreadPoolExecutor getInstance(){
        if(poolExecutor == null){
            // synchronized锁修饰符：锁定一个代码块或方法，保证线程安全（只有一个线程执行完该代码块或方法，下一个线程才能执行）
            synchronized(ThreadPoolUtil.class){
                if(poolExecutor == null){
                    System.out.println("~~~创建线程池~~~");
                    poolExecutor = new ThreadPoolExecutor(
                        4,
                        20,
                        300,
                        TimeUnit.SECONDS,
                        // 指定线程执行任务的序列，最大任务数指定为Integer.MAX_VALUE
                        // 任务的类型指定为Runnable接口的实现类，因为任务的类型必须run方法，实现Runnable接口可保证这一点
                        new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE)
                    );
                }
            }
        }
        return poolExecutor;
    }
}
