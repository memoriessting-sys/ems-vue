package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpPostLevel;
import cqie.edu.ems.domain.qo.EmpPostLevelQo;
import cqie.edu.ems.domain.vo.EmpPostLevelVo;
import cqie.edu.ems.service.EmpPostLevelService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postLevel")
public class EmpPostLevelController {

    @Autowired
    private EmpPostLevelService empPostLevelService;

    @PostMapping("/paged")
    public Result<PageInfo<EmpPostLevelVo>> paged(@RequestBody PageQo<EmpPostLevelQo> qo) {
        return Result.success(empPostLevelService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody EmpPostLevel postLevel) {
        if (empPostLevelService.existName(postLevel.getName(), postLevel.getId())) return Result.error("岗位名称已存在");
        if (postLevel.getId() == null) empPostLevelService.insert(postLevel);
        else empPostLevelService.update(postLevel);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        empPostLevelService.deleteById(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<EmpPostLevel> getById(@PathVariable Long id) {
        return Result.success(empPostLevelService.getById(id));
    }

    @GetMapping("/listAll")
    public Result<List<EmpPostLevel>> listAll() {
        return Result.success(empPostLevelService.list(new EmpPostLevelQo()));
    }
}