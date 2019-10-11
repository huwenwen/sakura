package com.wen.sakura.current;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
public final class NamedThreadFactory implements ThreadFactory {

    private static final String DEFAULT_NAME_PREFIX = "namedThreadFactory";

    @Getter
    private String threadNamePrefix;

    @Getter
    @Setter
    private boolean daemon;

    @Getter
    @Setter
    private int threadPriority = Thread.NORM_PRIORITY;

    @Setter
    @Getter
    private ThreadGroup threadGroup;

    private final AtomicInteger threadCount = new AtomicInteger(0);

    public NamedThreadFactory() {
        this.threadNamePrefix = DEFAULT_NAME_PREFIX;
    }

    public NamedThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    protected String nextThreadName() {
        return getThreadNamePrefix() + this.threadCount.incrementAndGet();
    }

    public Thread createThread(Runnable runnable) {
        Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
        thread.setPriority(getThreadPriority());
        thread.setDaemon(isDaemon());
        return thread;
    }

    @Override
    public Thread newThread(Runnable r) {
        return this.createThread(r);
    }
}
