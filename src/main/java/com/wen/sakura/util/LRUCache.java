package com.wen.sakura.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final int DEFAULT_CACHE_SIZE = 100;

    private int cacheSize;

    public LRUCache() {
        this(DEFAULT_CACHE_SIZE);
    }

    public LRUCache(int cacheSize) {
        super(16, 0.75f, true);
        this.cacheSize = cacheSize;
    }

    @Override
    public synchronized V put(K key, V value) {
        return super.put(key, value);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > cacheSize;
    }
}
