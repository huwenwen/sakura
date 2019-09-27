package com.wen.sakura.mybatis;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
public class PagerCountSupport {
    private static final String CQL_PREFIX = "select count(";
    private static final String CQL_SUFFIX = ") from ";

    public static String dealCount(String sql) {
        return String.format("select count(1) from ( %s ) total", sql);
    }

    private static String distinct(String sqlLower) {
        int from = sqlLower.indexOf("distinct");
        if (-1 == from) {
            return "1";
        }
        return sqlLower.substring(from, indexOfFrom(sqlLower));
    }

    private static int indexOfFrom(String sqlLower) {
        char[] cs = sqlLower.toCharArray();
        int ac = 0;
        int bc = 0;
        int fc = 0;
        for (int i = 0; i < cs.length; i++) {
            if ('(' == cs[i]) {
                ac++;
                continue;
            }
            if (')' == cs[i]) {
                bc++;
                continue;
            }
            if (ac == bc && i < cs.length - 3 && 'f' == cs[i] && 'r' == cs[i + 1] && 'o' == cs[i + 2] && 'm' == cs[i + 3]) {
                fc = i;
                break;
            }
        }
        return fc;
    }
}
