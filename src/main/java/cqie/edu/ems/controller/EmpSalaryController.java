package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpSalary;
import cqie.edu.ems.domain.qo.EmpSalaryQo;
import cqie.edu.ems.domain.vo.EmpSalaryVo;
import cqie.edu.ems.service.EmpSalaryService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salary")
public class EmpSalaryController {

    @Autowired
    private EmpSalaryService salaryService;

    @PostMapping("/paged")
    public Result<PageInfo<EmpSalaryVo>> paged(@RequestBody PageQo<EmpSalaryQo> qo, HttpServletRequest request) {
        Integer roleType = (Integer) request.getAttribute("roleType");
        if (roleType != 3) {
            Long empId = salaryService.getEmpIdByUserId((Long) request.getAttribute("userId"));
            if (empId != null) qo.getFilters().setEmp_id(empId);
            else {
                PageInfo<EmpSalaryVo> empty = new PageInfo<>();
                empty.setTotal(0);
                return Result.success(empty);
            }
        }
        return Result.success(salaryService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody EmpSalary salary) {
        if (salary.getId() == null) salaryService.insert(salary);
        else salaryService.update(salary);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        salaryService.deleteById(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<EmpSalary> getById(@PathVariable Long id) {
        return Result.success(salaryService.getById(id));
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics(HttpServletRequest request) {
        Integer roleType = (Integer) request.getAttribute("roleType");
        Map<String, Object> stats;
        if (roleType != 3) {
            Long empId = salaryService.getEmpIdByUserId((Long) request.getAttribute("userId"));
            stats = empId != null ? salaryService.getStatisticsByEmpId(empId) : defaultStats();
        } else {
            stats = salaryService.getStatistics();
        }
        return Result.success(stats);
    }

    @PostMapping("/batchPay")
    public Result<Void> batchPay(@RequestBody List<Long> ids) {
        salaryService.batchPay(ids);
        return Result.success(null);
    }

    private Map<String, Object> defaultStats() {
        Map<String, Object> m = new HashMap<>();
        m.put("totalSalaryMonthly", BigDecimal.ZERO);
        m.put("avgSalary", BigDecimal.ZERO);
        m.put("pendingSalaryCount", 0);
        m.put("paidSalaryCount", 0);
        return m;
    }
}