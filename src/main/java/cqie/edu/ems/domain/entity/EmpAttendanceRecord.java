package cqie.edu.ems.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 员工考勤记录(EmpAttendanceRecord)实体类
 *
 * @author
 * @since
 */
@Data

public class EmpAttendanceRecord implements Serializable {
    private static final long serialVersionUID = -76287425398602653L;
    /**
     * 考勤记录Id
     */
    private Long id;
    /**
     * 打卡人Id
     */
    private Long userId;
    /**
     * 员工姓名 - 用于直接输入员工姓名
     */
    private String userName;
    /**
     * 考勤日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;
    /**
     * 签到时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime checkInTime;
    /**
     * 签退时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime checkOutTime;
    /**
     * 考勤状态(0-正常 1-迟到 2-早退 3-迟到且早退)
     */
    private Integer state;
    /**
     * 部门Id
     */
    private Long departmentId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 部门名称（非数据库字段）
     */
    private String departmentName;
}
