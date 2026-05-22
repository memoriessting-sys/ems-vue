package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Autowired
    private EmpService empService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private EmpAttendanceRecordService attendanceService;
    @Autowired
    private EmpLeaveRecordService leaveService;
    @Autowired
    private EmpSalaryService salaryService;

    @GetMapping("/basicStats")
    public Result<Map<String, Object>> basicStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("employeeCount", empService.countAll());
        stats.put("departmentCount", deptService.countAll());
        stats.put("attendanceCount", attendanceService.countTodayAttendance());
        stats.put("leaveCount", leaveService.countPendingLeaves());
        return Result.success(stats);
    }

    @GetMapping("/employeeDistribution")
    public Result<List<Map<String, Object>>> employeeDistribution() {
        return Result.success(empService.getEmployeeDistributionByDept());
    }

    @GetMapping("/attendanceTrend")
    public Result<List<Map<String, Object>>> attendanceTrend(@RequestParam(defaultValue = "30") int days) {
        return Result.success(attendanceService.getAttendanceTrend(days));
    }

    @GetMapping("/salaryStats")
    public Result<Map<String, Object>> salaryStats(HttpServletRequest request) {
        Integer roleType = (Integer) request.getAttribute("roleType");
        Map<String, Object> stats;
        if (roleType != 3) {
            Long empId = salaryService.getEmpIdByUserId((Long) request.getAttribute("userId"));
            stats = empId != null ? salaryService.getStatisticsByEmpId(empId) : new HashMap<>();
        } else {
            stats = salaryService.getStatistics();
        }
        return Result.success(stats);
    }

    @GetMapping("/leaveStats")
    public Result<Map<String, Object>> leaveStats() {
        return Result.success(leaveService.getStatistics());
    }

    @GetMapping("/attendanceStats")
    public Result<Map<String, Object>> attendanceStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("normalCount", attendanceService.countByState(0));
        stats.put("lateCount", attendanceService.countByState(1));
        stats.put("earlyCount", attendanceService.countByState(2));
        stats.put("bothCount", attendanceService.countByState(3));
        return Result.success(stats);
    }
}