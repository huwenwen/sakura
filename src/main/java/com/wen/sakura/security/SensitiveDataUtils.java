package com.wen.sakura.security;

import com.wen.sakura.util.Helper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 敏感数据相关工具类
 *
 * @author huwenwen
 * @date 2019/10/9
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SensitiveDataUtils {

    /**
     * 对字符串数据脱敏
     * <p>
     * 常用作：
     * 手机号显示前4位后3位
     * 身份证
     *
     * @param sensitiveData   原始数据
     * @param displayFrontNum 展示前几位
     * @param displayTailNum  展示后几位
     * @return
     */
    public static String mask(String sensitiveData, int displayFrontNum, int displayTailNum) {
        if (Helper.isEmpty(sensitiveData)) {
            return sensitiveData;
        }
        int length = sensitiveData.length();
        if (displayFrontNum < 0 || displayTailNum < 0 || displayFrontNum + displayTailNum > length) {
            return sensitiveData;
        }
        int beginIndex = displayFrontNum;
        int endIndex = length - displayTailNum;
        char[] result = new char[length];
        // front
        sensitiveData.getChars(0, beginIndex, result, 0);
        // mid
        for (int i = beginIndex; i < endIndex; i++) {
            result[i] = '*';
        }
        // tail
        sensitiveData.getChars(endIndex, length, result, endIndex);
        return new String(result);
    }

    /**
     * 对字符串数据脱敏
     *
     * @param sensitiveData
     * @param displayFrontNum
     * @param displayTailNum
     * @param hideNum         隐藏符个数
     * @return
     */
    public static String mask(String sensitiveData, int displayFrontNum, int displayTailNum, int hideNum) {
        if (Helper.isEmpty(sensitiveData)) {
            return sensitiveData;
        }
        int length = sensitiveData.length();
        int markLength = displayFrontNum + displayTailNum;
        if (displayFrontNum < 0 || displayTailNum < 0 || markLength > length) {
            return sensitiveData;
        }
        if (markLength == length) {
            return sensitiveData;
        }
        int beginIndex = displayFrontNum;
        int endIndex = displayFrontNum + hideNum;
        int resultLength = markLength + hideNum;
        char[] result = new char[resultLength];
        // front
        sensitiveData.getChars(0, beginIndex, result, 0);
        // mid
        for (int i = beginIndex; i < endIndex; i++) {
            result[i] = '*';
        }
        // tail
        sensitiveData.getChars(length - displayTailNum, length, result, endIndex);
        return new String(result);
    }
}
