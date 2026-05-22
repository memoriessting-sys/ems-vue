package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.Emp;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class EmpVo extends Emp {

    private String deptName; // 部门名称
    private String postLevelName; // 岗位岗级名称
    private String userName; // 绑定用户名称
    private String sexName; // 性别名称
    private String maritalStatusName; // 婚姻状态名称
    private String statusName; // 状态名称
}