package cqie.edu.ems.domain.qo;

import lombok.Data;
import java.util.Date;

@Data
public class EmpLeaveRecordQo {
    private Long user_id;
    private Date createTimeStart;
    private Date createTimeEnd;
    private Date leaveStartTimeStart;
    private Date leaveStartTimeEnd;
    private Date leaveEndTimeStart;
    private Date leaveEndTimeEnd;
    private String leaveTypeCode;
    private Integer status;
}