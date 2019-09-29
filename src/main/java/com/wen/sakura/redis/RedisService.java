package com.wen.sakura.redis;

import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;

/**
 * @author huwenwen
 * @date 2019/9/29
 */
public class RedisService {

    @Setter
    private JedisPool jedisPool;

    /**
     * 1ms = 1000 * 1000 ns
     */
    private static final long MILLI_NANO_CONVERSION = 1000 * 1000L;

    private static final long SECOND_NANO_CONVERSION = MILLI_NANO_CONVERSION * 1000;
    /**
     * 锁的超时时间（秒），过期删除
     */
    private static final long LOCK_EXPIRE = 60L;
    /**
     * setnx设置超时时间脚本
     **/
    private static final String SETNX_EXPIRE_SCRIPT = "local ret = redis.call('setnx', KEYS[1], ARGV[1]);"
            + "if ret == 1 then"
            + " redis.call('expire', KEYS[1], ARGV[2]) "
            + "end;"
            + "return ret;";

    public boolean lock(String key, long timeout) {
        long begin = System.nanoTime();
        try (Jedis jedis = getJedis()) {
            timeout *= SECOND_NANO_CONVERSION;
            while ((System.nanoTime() - begin) < timeout) {
                List<String> keys = Arrays.asList(key);
                List<String> args = Arrays.asList(String.valueOf(System.nanoTime()), String.valueOf(LOCK_EXPIRE));
                Long ret = (Long) jedis.eval(SETNX_EXPIRE_SCRIPT, keys, args);
                if (ret == 1L) {
                    return true;
                }
                String val = jedis.get(key);
                if (val == null) {
                    continue;
                }
                // block
                Thread.sleep(1L);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void unlock(String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(key);
        }
    }

    /**
     * 获取 Jedis
     * <p>
     * 需要在finally里面释放资源 {@link #close(Jedis)}
     * 或者 使用 try (Jedis jedis = getJedis())
     *
     * @return
     */
    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    private void close(Jedis jedis) {
        jedis.close();
    }

}
