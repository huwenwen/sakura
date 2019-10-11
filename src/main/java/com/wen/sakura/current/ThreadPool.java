package com.wen.sakura.current;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
public class ThreadPool {

    /**
     * ThreadPoolExecutor
     * corePoolSize: 核心线程数，一直存在
     * maximumPoolSize: 最大线程数，当workQueue满了添加新的线程，直到最大值
     * keepAliveTime: maxPoolSize保留时间
     * workQueue: 核心线程满了，会offer队列,本身是一个blockQueue，可重写offer为put实现阻塞队列
     * <p>
     * execute步骤
     * step1.调用ThreadPoolExecutor的execute提交线程，首先检查CorePool，如果CorePool内的线程小于CorePoolSize，新创建线程执行任务。
     * step2.如果当前CorePool内的线程大于等于CorePoolSize，那么将线程加入到BlockingQueue。
     * step3.如果不能加入BlockingQueue，在小于MaxPoolSize的情况下创建线程执行任务。
     * step4.如果线程数大于等于MaxPoolSize，那么执行拒绝策略。
     *
     * @param fixed
     * @return
     */
    private static ExecutorService getThreadPool(int fixed) {
        int processorsOfCPU = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2 * processorsOfCPU,
                2 * processorsOfCPU,
                5L,
                TimeUnit.MINUTES,
                new LimitedQueue<>(fixed),
                new NamedThreadFactory("process-multi-execute-thread"));
        return threadPool;
    }

    public static ExecutorService create(int fixed) {
        return getThreadPool(fixed);
    }
}
