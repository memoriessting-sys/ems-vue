package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.EmpLeaveRecord;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class EmpLeaveRecordVo extends EmpLeaveRecord {
    private String userName;
    private String approveUserName;
    private String leaveTypeName;
    private String statusName;
}