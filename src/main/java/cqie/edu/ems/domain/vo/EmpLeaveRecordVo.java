package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.EmpLeaveRecord;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class EmpLeaveRecordVo extends EmpLeaveRecord {
    private String user_name;
    private String approve_user_name;
    private String leave_type_name;
    private String status_name;
}