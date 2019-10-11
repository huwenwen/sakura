package com.wen.sakura.current;

import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
public class ThreadTest {

    @Test
    public void threadPoolTest(){
        ThreadPoolExecutor executorService = (ThreadPoolExecutor)ThreadPool.create(1);
        while (true) {
//            if (executorService.getCompletedTaskCount() > 100) {
//                break;
//            }
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                System.out.println(executorService.toString());
            });
        }
    }

}
