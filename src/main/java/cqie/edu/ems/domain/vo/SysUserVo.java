package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.SysUser;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SysUserVo extends SysUser {
    private String statusName;// 状态显示值
    private String deptName;// 部门名称
    private String roleTypeName;// 角色类型显示值
}
