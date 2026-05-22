package cqie.edu.ems.domain.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工薪资信息表(EmpSalary)实体类
 */
@Data
public class EmpSalary implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 员工ID
     */
    private Long emp_id;

    /**
     * 薪资年月
     */
    private String salary_year_month;

    /**
     * 基本工资
     */
    private BigDecimal base_salary;

    /**
     * 岗位工资
     */
    private BigDecimal post_salary;

    /**
     * 绩效工资
     */
    private BigDecimal performance_salary;

    /**
     * 加班工资
     */
    private BigDecimal overtime_salary;

    /**
     * 补贴
     */
    private BigDecimal allowance;

    /**
     * 奖金
     */
    private BigDecimal bonus;

    /**
     * 扣款
     */
    private BigDecimal deduction;

    /**
     * 实发工资
     */
    private BigDecimal actual_salary;

    /**
     * 发放状态(0-未发放 1-已发放)
     */
    private Integer status;

    /**
     * 发放时间
     */
    private Date pay_time;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}