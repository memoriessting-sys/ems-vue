package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.SysDict;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SysDictVo extends SysDict {
    private String statusName;//状态显示值
}
