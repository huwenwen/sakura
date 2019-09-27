package com.wen.sakura.mybatis;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
public interface IPage {
    void setIndex(Integer index);
    void setLimit(Integer limit);
    void setOrderBy(String orderBy);
    void setTotal(Integer total);
    void setData(Object data);
}
