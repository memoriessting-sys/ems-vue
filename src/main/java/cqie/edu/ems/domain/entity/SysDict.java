package cqie.edu.ems.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统字典(SysDict)实体类
 */
@Data
public class SysDict implements Serializable {
    private static final long serialVersionUID = -28799300383711128L;
    /**
     * 字典项Id
     */
    private Integer id;
    /**
     * 字典类别
     */
    private String dictType;
    /**
     * 字典项编码
     */
    private String dictItemKey;
    /**
     * 字典项值
     */
    private String dictItemValue;
    /**
     * 排序码
     */
    private Integer sortCode;
    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;

}
