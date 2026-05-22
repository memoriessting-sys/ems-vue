package cqie.edu.ems.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 部门表(SysDept)实体类
 */
@Data
public class SysDept implements Serializable {
    private static final long serialVersionUID = 577963187321376917L;
    /**
     * 部门Id
     */
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 负责人Id
     */
    private Long manager_user_id;
    /**
     * 上级部门Id
     */
    private Long parent_id;
    /**
     * 排序码
     */
    private Integer sort_code;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;

}
