package com.wen.sakura.util;

import java.util.concurrent.ThreadLocalRandom;

public class SnowflakeIdWorker {

    /**
     * 开始时间戳，可根据当前时间来
     */
    private static final long EPOCH = 1240000000000L;
    // 5bit位机房
    private static final long DATACENTER_ID_BITS = 5L;
    // 2bit位类型
    private static final long TYPE_ID_BITS = 2L;
    // 15bit位随机数
    private static final long SEQUENCE_BITS = 15;
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);
    private static final long TYPE_LEFTSHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_LEFTSHIFT = TYPE_LEFTSHIFT + TYPE_ID_BITS;
    private static final long TIMESTAMP_LEFTSHIFT = DATACENTER_ID_LEFTSHIFT + DATACENTER_ID_BITS;

    /**
     * https://github.com/twitter-archive/snowflake/blob/b3f6a3c6ca8e1b6847baa6ff42bf72201e2c2231/src/main/scala/com/twitter/service/snowflake/IdWorker.scala
     * snowflake思想: 41bit作为毫秒数,10bit位作为机器ID(5bit位是数据中心,5bit机器ID),12bit作为毫秒内的流水号
     *
     * @param dataCenter
     * @param type
     * @return 41bit(时间戳) + 5bit(机房) + 2bit(类型) + 15bit(随机数)
     */
    public static long snowflake(int dataCenter, int type) {
        long time = System.currentTimeMillis() - EPOCH;
        long sequence = ThreadLocalRandom.current().nextLong(MAX_SEQUENCE + 1);
        long orderId = (time << TIMESTAMP_LEFTSHIFT) | (dataCenter << DATACENTER_ID_LEFTSHIFT)
                | (type << TYPE_LEFTSHIFT) | sequence;
        return orderId;
    }

}
