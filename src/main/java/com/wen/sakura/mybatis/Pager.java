package com.wen.sakura.mybatis;

import lombok.Data;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
@Data
public class Pager implements IPager {

    private Integer limit = 10;

    private Integer index = 1;

    private Integer total = 0;

    private String orderBy = "";

    private Integer pageNum = 0;

    public void setIndex(Integer index) {
        this.index = index;
        calculate();
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
        calculate();
    }

    public void setTotal(Integer total) {
        this.total = total;
        calculate();
    }

    private void calculate() {
        if (0 != limit) {
            this.pageNum = total % limit == 0 ? total / limit : total / limit + 1;
        }
    }
}
