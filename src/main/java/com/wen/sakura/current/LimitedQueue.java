package com.wen.sakura.current;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
public class LimitedQueue<E> extends LinkedBlockingQueue<E> {
    public LimitedQueue(int maxSize) {
        super(maxSize);
    }

    /**
     * 重写offer为put
     * ThreadPoolExecutor里面调用的是offer进queue，重写为put后为阻塞队列，可以一直等待
     *
     * @param e
     * @return
     */
    @Override
    public boolean offer(E e) {
        try {
            put(e);
            return true;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
