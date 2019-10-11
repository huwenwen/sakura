package com.wen.sakura;

import com.wen.sakura.util.LRUCache;
import org.junit.Test;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
public class CacheTest {

    @Test
    public void lru() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);
        cache.put(3, 3);
        cache.keySet().forEach(System.out::println);
    }

}
