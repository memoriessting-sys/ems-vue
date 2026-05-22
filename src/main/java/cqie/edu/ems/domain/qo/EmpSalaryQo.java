package cqie.edu.ems.domain.qo;

import lombok.Data;
import java.util.Date;

/**
 * 员工薪资查询对象
 */
@Data
public class EmpSalaryQo {
    /**
     * 员工ID
     */
    private Long emp_id;

    /**
     * 薪资年月
     */
    private String salary_year_month;

    /**
     * 发放状态(0-未发放 1-已发放)
     */
    private Integer status;

    /**
     * 发放开始时间
     */
    private Date pay_time_start;

    /**
     * 发放结束时间
     */
    private Date pay_time_end;

    /**
     * 创建开始时间
     */
    private Date create_time_start;

    /**
     * 创建结束时间
     */
    private Date create_time_end;
}