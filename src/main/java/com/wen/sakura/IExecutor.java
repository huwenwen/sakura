package com.wen.sakura;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
@FunctionalInterface
public interface IExecutor<T> {
    /**
     * execute something
     *
     * @return
     * @throws Throwable
     */
    T exe() throws Throwable;
}
