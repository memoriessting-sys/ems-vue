package cqie.edu.ems.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 岗位岗级表(EmpPostLevel)实体类
 */
@Data
public class EmpPostLevel implements Serializable {
    private static final long serialVersionUID = -90085086603991223L;
    /**
     * 岗位岗级Id
     */
    private Long id;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 岗级
     */
    private Integer level;
    /**
     * 岗位工资
     */
    private Double salary;
    /**
     * 排序码
     */
    private Integer sort_code;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;

}
