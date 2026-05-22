package cqie.edu.ems.domain.qo;

import lombok.Data;

@Data
public class EmpQo {
    private String name; // 姓名
    private String code; // 工号
    private String mobile; // 手机号
    private Long deptId; // 部门ID
    private Long postLevelId; // 岗位岗级ID
    private Integer status; // 状态

    private String namePattern; // 姓名模糊匹配
    private String codePattern; // 工号模糊匹配
    private String mobilePattern; // 手机号模糊匹配
}