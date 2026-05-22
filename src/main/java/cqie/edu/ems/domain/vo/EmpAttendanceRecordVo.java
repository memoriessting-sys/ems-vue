package cqie.edu.ems.domain.vo;

import cqie.edu.ems.domain.entity.EmpAttendanceRecord;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 员工考勤记录表(EmpAttendanceRecordVo)值对象
 */
@Data
@ToString(callSuper = true)
public class EmpAttendanceRecordVo extends EmpAttendanceRecord {
    /**
     * 员工姓名 - 用于直接输入员工姓名
     */
    private String userName;
    /**
     * 考勤日期
     */
    private LocalDate attendanceDate;
    /**
     * 签到时间
     */
    private LocalTime checkInTime;
    /**
     * 签退时间
     */
    private LocalTime checkOutTime;
    /**
     * 考勤状态(0-正常 1-迟到 2-早退 3-迟到且早退)
     */
    private Integer state;
    /**
     * 考勤状态名称
     */
    private String stateName;
    /**
     * 工作时长(小时)
     */
    private Double workHours;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 备注
     */
    private String remark;
}