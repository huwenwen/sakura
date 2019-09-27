package com.wen.sakura.mybatis;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
@Data
public class Page<T> extends Pager implements IPage, Serializable {

    private static final long serialVersionUID = -3804461240807349107L;
    private List<T> data;

    @Override
    public void setData(Object data) {
        this.data = (List<T>) data;
    }
}
