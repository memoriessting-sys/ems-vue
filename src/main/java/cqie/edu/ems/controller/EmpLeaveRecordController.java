package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpLeaveRecord;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.EmpLeaveRecordQo;
import cqie.edu.ems.service.EmpLeaveRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class EmpLeaveRecordController {

    @Autowired
    private EmpLeaveRecordService leaveService;

    @PostMapping("/paged")
    public Result<Map<String, Object>> paged(@RequestBody PageQo<EmpLeaveRecordQo> qo, HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) return Result.error("未登录");
        if (user.getRoleType() != 3) qo.getFilters().setUser_id(user.getId());
        return Result.success(leaveService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody EmpLeaveRecord record, HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) return Result.error("未登录");
        if (user.getRoleType() != 3) {
            record.setUser_id(user.getId());
            record.setStatus(1);
        }
        if (record.getId() == null) leaveService.insert(record);
        else leaveService.update(record);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        leaveService.deleteById(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<EmpLeaveRecord> getById(@PathVariable Long id) {
        return Result.success(leaveService.getById(id));
    }

    @PostMapping("/approve")
    public Result<Void> approve(@RequestParam Long leaveId, @RequestParam Integer status, @RequestParam String approveContent) {
        leaveService.approveLeave(leaveId, status, approveContent);
        return Result.success(null);
    }

    @PostMapping("/return/{id}")
    public Result<Void> handleReturn(@PathVariable Long id) {
        leaveService.handleReturn(id);
        return Result.success(null);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) return Result.error("未登录");
        return Result.success(leaveService.getStatistics());
    }
}