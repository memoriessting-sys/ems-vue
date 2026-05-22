package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpSalary;
import cqie.edu.ems.domain.qo.EmpSalaryQo;
import cqie.edu.ems.domain.vo.EmpSalaryVo;
import cqie.edu.ems.mapper.EmpSalaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工薪资服务层
 */
@Service
public class EmpSalaryService {
    @Autowired
    private EmpSalaryMapper empSalaryMapper;

    /**
     * 分页查询
     */
    public PageInfo<EmpSalaryVo> paged(PageQo<EmpSalaryQo> qo) {
        try {
            if (qo == null) {
                qo = new PageQo<>();
                qo.setPageIndex(1);
                qo.setPageSize(10);
            }

            if (qo.getPageIndex() <= 0) {
                qo.setPageIndex(1);
            }

            if (qo.getPageSize() <= 0) {
                qo.setPageSize(10);
            }

            PageHelper.startPage(qo.getPageIndex(), qo.getPageSize());
            List<EmpSalaryVo> list = empSalaryMapper.paged(qo.getFilters());

            // 确保返回的列表不为null
            if (list == null) {
                list = new java.util.ArrayList<>();
            }

            return new PageInfo<>(list);
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回空列表
            List<EmpSalaryVo> emptyList = new java.util.ArrayList<>();
            return new PageInfo<>(emptyList);
        }
    }

    /**
     * 获取所有薪资记录
     */
    public List<EmpSalaryVo> listAll() {
        try {
            // 创建一个空的查询条件
            EmpSalaryQo qo = new EmpSalaryQo();
            return empSalaryMapper.paged(qo);
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回空列表
            return new java.util.ArrayList<>();
        }
    }

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(EmpSalary empSalary) {
        // 检查是否已存在该员工该月的薪资记录
        EmpSalary existSalary = empSalaryMapper.getByEmpIdAndYearMonth(empSalary.getEmp_id(),
                empSalary.getSalary_year_month());
        if (existSalary != null) {
            throw new RuntimeException("该员工" + empSalary.getSalary_year_month() + "的薪资记录已存在");
        }

        // 计算实发工资
        calculateActualSalary(empSalary);

        empSalaryMapper.insert(empSalary);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(EmpSalary empSalary) {
        // 计算实发工资
        calculateActualSalary(empSalary);

        empSalaryMapper.update(empSalary);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        empSalaryMapper.deleteById(id);
    }

    /**
     * 根据ID获取
     */
    public EmpSalary getById(Long id) {
        return empSalaryMapper.getById(id);
    }

    /**
     * 计算实发工资
     */
    private void calculateActualSalary(EmpSalary empSalary) {
        BigDecimal actualSalary = BigDecimal.ZERO;

        // 基本工资
        if (empSalary.getBase_salary() != null) {
            actualSalary = actualSalary.add(empSalary.getBase_salary());
        }

        // 岗位工资
        if (empSalary.getPost_salary() != null) {
            actualSalary = actualSalary.add(empSalary.getPost_salary());
        }

        // 绩效工资
        if (empSalary.getPerformance_salary() != null) {
            actualSalary = actualSalary.add(empSalary.getPerformance_salary());
        }

        // 加班工资
        if (empSalary.getOvertime_salary() != null) {
            actualSalary = actualSalary.add(empSalary.getOvertime_salary());
        }

        // 补贴
        if (empSalary.getAllowance() != null) {
            actualSalary = actualSalary.add(empSalary.getAllowance());
        }

        // 奖金
        if (empSalary.getBonus() != null) {
            actualSalary = actualSalary.add(empSalary.getBonus());
        }

        // 扣款
        if (empSalary.getDeduction() != null) {
            actualSalary = actualSalary.subtract(empSalary.getDeduction());
        }

        empSalary.setActual_salary(actualSalary);
    }

    /**
     * 获取薪资统计数据
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取当前月份
            String currentMonth = java.time.LocalDate.now().toString().substring(0, 7);

            // 获取本月薪资总额
            BigDecimal totalSalaryMonthly = null;
            try {
                totalSalaryMonthly = empSalaryMapper.getTotalSalaryByMonth(currentMonth);
            } catch (Exception e) {
                e.printStackTrace();
                totalSalaryMonthly = BigDecimal.ZERO;
            }
            result.put("totalSalaryMonthly", totalSalaryMonthly != null ? totalSalaryMonthly : BigDecimal.ZERO);

            // 获取平均薪资
            BigDecimal avgSalary = null;
            try {
                avgSalary = empSalaryMapper.getAvgSalary();
            } catch (Exception e) {
                e.printStackTrace();
                avgSalary = BigDecimal.ZERO;
            }
            result.put("avgSalary", avgSalary != null ? avgSalary.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

            // 获取待发放薪资记录数
            Integer pendingSalaryCount = null;
            try {
                pendingSalaryCount = empSalaryMapper.getSalaryCountByStatus(0);
            } catch (Exception e) {
                e.printStackTrace();
                pendingSalaryCount = 0;
            }
            result.put("pendingSalaryCount", pendingSalaryCount != null ? pendingSalaryCount : 0);

            // 获取已发放薪资记录数
            Integer paidSalaryCount = null;
            try {
                paidSalaryCount = empSalaryMapper.getSalaryCountByStatus(1);
            } catch (Exception e) {
                e.printStackTrace();
                paidSalaryCount = 0;
            }
            result.put("paidSalaryCount", paidSalaryCount != null ? paidSalaryCount : 0);
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时设置默认值
            result.put("totalSalaryMonthly", BigDecimal.ZERO);
            result.put("avgSalary", BigDecimal.ZERO);
            result.put("pendingSalaryCount", 0);
            result.put("paidSalaryCount", 0);
        }

        return result;
    }

    /**
     * 批量发放薪资
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchPay(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 当前时间作为发放时间
        Date now = new Date();

        for (Long id : ids) {
            EmpSalary empSalary = empSalaryMapper.getById(id);
            if (empSalary != null && empSalary.getStatus() != null && empSalary.getStatus() == 0) {
                empSalary.setStatus(1); // 设置为已发放
                empSalary.setPay_time(now); // 设置发放时间
                empSalaryMapper.update(empSalary);
            }
        }
    }

    /**
     * 根据用户ID获取员工ID
     */
    public Long getEmpIdByUserId(Long userId) {
        try {
            return empSalaryMapper.getEmpIdByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据员工ID获取薪资记录
     */
    public List<EmpSalaryVo> getByEmpId(Long empId) {
        try {
            EmpSalaryQo qo = new EmpSalaryQo();
            qo.setEmp_id(empId);
            return empSalaryMapper.paged(qo);
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    /**
     * 根据员工ID获取薪资统计
     */
    public Map<String, Object> getStatisticsByEmpId(Long empId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取当前月份
            String currentMonth = java.time.LocalDate.now().toString().substring(0, 7);

            // 获取本月薪资总额
            BigDecimal totalSalaryMonthly = null;
            try {
                totalSalaryMonthly = empSalaryMapper.getTotalSalaryByEmpIdAndMonth(empId, currentMonth);
            } catch (Exception e) {
                e.printStackTrace();
                totalSalaryMonthly = BigDecimal.ZERO;
            }
            result.put("totalSalaryMonthly", totalSalaryMonthly != null ? totalSalaryMonthly : BigDecimal.ZERO);

            // 获取平均薪资
            BigDecimal avgSalary = null;
            try {
                avgSalary = empSalaryMapper.getAvgSalaryByEmpId(empId);
            } catch (Exception e) {
                e.printStackTrace();
                avgSalary = BigDecimal.ZERO;
            }
            result.put("avgSalary", avgSalary != null ? avgSalary.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

            // 获取待发放薪资记录数
            Integer pendingSalaryCount = null;
            try {
                pendingSalaryCount = empSalaryMapper.getPendingSalaryCountByEmpId(empId);
            } catch (Exception e) {
                e.printStackTrace();
                pendingSalaryCount = 0;
            }
            result.put("pendingSalaryCount", pendingSalaryCount != null ? pendingSalaryCount : 0);

            // 获取已发放薪资记录数
            Integer paidSalaryCount = null;
            try {
                paidSalaryCount = empSalaryMapper.getPaidSalaryCountByEmpId(empId);
            } catch (Exception e) {
                e.printStackTrace();
                paidSalaryCount = 0;
            }
            result.put("paidSalaryCount", paidSalaryCount != null ? paidSalaryCount : 0);

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回默认值
            result.put("totalSalaryMonthly", BigDecimal.ZERO);
            result.put("avgSalary", BigDecimal.ZERO);
            result.put("pendingSalaryCount", 0);
            result.put("paidSalaryCount", 0);
        }

        return result;
    }
}