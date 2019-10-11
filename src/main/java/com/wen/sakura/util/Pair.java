package com.wen.sakura.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
@Data
@AllArgsConstructor
public class Pair<K, V> implements Serializable {
    private static final long serialVersionUID = 4600778615527838632L;

    private K key;
    private V value;

}
