package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.Emp;
import cqie.edu.ems.domain.qo.EmpQo;
import cqie.edu.ems.domain.vo.EmpVo;
import cqie.edu.ems.service.EmpService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emp")
public class EmpController {

    @Autowired
    private EmpService empService;

    @PostMapping("/paged")
    public Result<PageInfo<EmpVo>> paged(@RequestBody PageQo<EmpQo> qo) {
        return Result.success(empService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Emp emp) {
        if (empService.existCode(emp.getCode(), emp.getId())) return Result.error("员工工号已存在");
        if (emp.getId() == null) empService.insert(emp);
        else empService.update(emp);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        empService.deleteById(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<Emp> getById(@PathVariable Long id) {
        return Result.success(empService.getById(id));
    }

    @GetMapping("/listAll")
    public Result<List<Emp>> listAll() {
        return Result.success(empService.list(new EmpQo()));
    }
}