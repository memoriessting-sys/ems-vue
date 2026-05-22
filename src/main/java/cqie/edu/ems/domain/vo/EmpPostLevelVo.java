package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.EmpPostLevel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class EmpPostLevelVo extends EmpPostLevel {

    private String statusName;
}
