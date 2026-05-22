package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.EmpSalary;
import cqie.edu.ems.domain.qo.EmpSalaryQo;
import cqie.edu.ems.domain.vo.EmpSalaryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

/**
 * 员工薪资信息表(EmpSalary)数据访问层
 */
@Repository
public interface EmpSalaryMapper {
    /**
     * 分页查询
     */
    List<EmpSalaryVo> paged(@Param("qo") EmpSalaryQo qo);

    /**
     * 新增
     */
    int insert(EmpSalary empSalary);

    /**
     * 修改
     */
    int update(EmpSalary empSalary);

    /**
     * 根据主键删除
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据主键获取
     */
    EmpSalary getById(@Param("id") Long id);

    /**
     * 根据员工ID和薪资年月获取
     */
    EmpSalary getByEmpIdAndYearMonth(@Param("emp_id") Long emp_id,
            @Param("salary_year_month") String salary_year_month);

    /**
     * 获取指定月份的薪资总额
     */
    BigDecimal getTotalSalaryByMonth(@Param("month") String month);

    /**
     * 获取平均薪资
     */
    BigDecimal getAvgSalary();

    /**
     * 根据状态获取薪资记录数量
     */
    Integer getSalaryCountByStatus(@Param("status") Integer status);

    /**
     * 根据用户ID获取员工ID
     */
    Long getEmpIdByUserId(@Param("userId") Long userId);

    /**
     * 根据员工ID和月份获取薪资总额
     */
    BigDecimal getTotalSalaryByEmpIdAndMonth(@Param("emp_id") Long emp_id, @Param("month") String month);

    /**
     * 根据员工ID获取平均薪资
     */
    BigDecimal getAvgSalaryByEmpId(@Param("emp_id") Long emp_id);

    /**
     * 根据员工ID获取待发放薪资记录数
     */
    Integer getPendingSalaryCountByEmpId(@Param("emp_id") Long emp_id);

    /**
     * 根据员工ID获取已发放薪资记录数
     */
    Integer getPaidSalaryCountByEmpId(@Param("emp_id") Long emp_id);
}