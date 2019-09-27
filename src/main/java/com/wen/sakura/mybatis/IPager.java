package com.wen.sakura.mybatis;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
public interface IPager {

    /**
     * index
     *
     * @return
     */
    Integer getIndex();

    /**
     * limit条数
     *
     * @return
     */
    Integer getLimit();

    /**
     * 排序字段
     *
     * @return
     */
    default String getOrderBy() {
        return null;
    }

}
