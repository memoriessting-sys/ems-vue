package cqie.edu.ems.domain.qo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 员工考勤记录查询对象
 */
@Data
public class EmpAttendanceRecordQo {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 员工姓名（用于模糊搜索）
     */
    private String empName;

    /**
     * 考勤开始日期
     */
    private LocalDate attendanceDateStart;

    /**
     * 考勤结束日期
     */
    private LocalDate attendanceDateEnd;

    /**
     * 考勤状态
     */
    private Integer state;

    /**
     * 部门ID
     */
    private Long departmentId;
}