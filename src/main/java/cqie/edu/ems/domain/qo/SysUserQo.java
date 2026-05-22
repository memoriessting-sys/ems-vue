package cqie.edu.ems.domain.qo;

import lombok.Data;

//用户列表的查询条件
@Data
public class SysUserQo {
    private String name;// 用户名-模糊查询
    private String account;// 登录账号-模糊查询
    private Integer status;// 状态(0-正常 1-禁用)
    private Integer roleType;// 角色类型（1-普通员工 2-主管 3-管理员）
    private Long deptId;// 部门ID
}
