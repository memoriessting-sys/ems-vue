package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpAttendanceRecord;
import cqie.edu.ems.domain.entity.SysDept;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.EmpAttendanceRecordQo;
import cqie.edu.ems.domain.vo.EmpAttendanceRecordVo;
import cqie.edu.ems.mapper.SysDeptMapper;
import cqie.edu.ems.mapper.SysUserMapper;
import cqie.edu.ems.service.EmpAttendanceRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class EmpAttendanceRecordController {

    @Autowired
    private EmpAttendanceRecordService attendanceService;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @PostMapping("/paged")
    public Result<PageInfo<EmpAttendanceRecordVo>> paged(@RequestBody PageQo<EmpAttendanceRecordQo> qo) {
        return Result.success(attendanceService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody EmpAttendanceRecord record) {
        if (record.getId() == null) attendanceService.insert(record);
        else attendanceService.update(record);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        attendanceService.deleteById(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<EmpAttendanceRecord> getById(@PathVariable Long id) {
        return Result.success(attendanceService.getById(id));
    }

    @PostMapping("/checkIn")
    public Result<Void> checkIn(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) return Result.error("请先登录");
        attendanceService.checkIn(user.getId());
        return Result.success(null);
    }

    @PostMapping("/checkOut")
    public Result<Void> checkOut(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) return Result.error("请先登录");
        attendanceService.checkOut(user.getId());
        return Result.success(null);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("normalCount", attendanceService.countByState(0));
        stats.put("lateCount", attendanceService.countByState(1));
        stats.put("earlyCount", attendanceService.countByState(2));
        stats.put("bothCount", attendanceService.countByState(3));
        return Result.success(stats);
    }

    @GetMapping("/departments")
    public Result<List<SysDept>> departments() {
        return Result.success(sysDeptMapper.list(null));
    }

    @GetMapping("/employees")
    public Result<List<SysUser>> employees() {
        return Result.success(sysUserMapper.list(null));
    }
}