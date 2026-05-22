package cqie.edu.ems.domain.vo;


import cqie.edu.ems.domain.entity.SysDept;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SysDeptVo extends SysDept {
    private String managerUserName;//负责人
    private String parentName;//上级部门名称
    private String statusName;//状态显示值
}
