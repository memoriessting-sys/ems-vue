package cqie.edu.ems.domain.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户信息表(SysUser)实体类
 */
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = -63182736067116633L;

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 登录账户
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 状态(0-正常 1-禁用)
     */
    private Integer status;

    /**
     * 角色类型（1-普通员工 2-主管 3-管理员）
     */
    private Integer roleType;

    /**
     * 验证码（非数据库字段，仅用于登录验证）
     */
    private String captcha;

    /**
     * 部门名称（非数据库字段，仅用于显示）
     */
    private String deptName;
}
