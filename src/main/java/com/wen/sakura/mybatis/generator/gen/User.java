package com.wen.sakura.mybatis.generator.gen;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表
 **/
@Data
@NoArgsConstructor
public class User {
    /** id */
    private Long id;

    /** 用户名 */
    private String userName;

    /** 手机号 */
    private String mobile;

    /** 删除标识 1删除 */
    private Integer isDelete;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}