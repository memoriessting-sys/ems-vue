package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysDept;
import cqie.edu.ems.domain.qo.SysDeptQo;
import cqie.edu.ems.domain.vo.SysDeptVo;
import cqie.edu.ems.domain.vo.TreeNodeDept;
import cqie.edu.ems.service.DeptService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dept")
public class SysDeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping("/paged")
    public Result<PageInfo<SysDeptVo>> paged(@RequestBody PageQo<SysDeptQo> qo) {
        return Result.success(deptService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysDept dept) {
        if (dept.getId() == null) deptService.insert(dept);
        else deptService.update(dept);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteById(id);
        return Result.success(null);
    }

    @PostMapping("/tree")
    public Result<List<TreeNodeDept>> tree(@RequestParam(value = "excludeRootId", required = false) Long excludeRootId) {
        return Result.success(deptService.listOptionForParent(excludeRootId));
    }

    @GetMapping("/listAll")
    public Result<List<SysDept>> listAll() {
        return Result.success(deptService.getAllDepts());
    }
}