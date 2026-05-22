package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpLeaveRecord;
import cqie.edu.ems.domain.qo.EmpLeaveRecordQo;
import cqie.edu.ems.service.EmpLeaveRecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class EmpLeaveRecordController {

    @Autowired
    private EmpLeaveRecordService leaveService;

    @PostMapping("/paged")
    public Result<Map<String, Object>> paged(@RequestBody PageQo<EmpLeaveRecordQo> qo, HttpServletRequest request) {
        Integer roleType = (Integer) request.getAttribute("roleType");
        if (roleType != 3) qo.getFilters().setUser_id((Long) request.getAttribute("userId"));
        return Result.success(leaveService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody EmpLeaveRecord record, HttpServletRequest request) {
        Integer roleType = (Integer) request.getAttribute("roleType");
        if (roleType != 3) {
            record.setUser_id((Long) request.getAttribute("userId"));
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
    public Result<Map<String, Object>> statistics(HttpServletRequest request) {
        return Result.success(leaveService.getStatistics());
    }
}