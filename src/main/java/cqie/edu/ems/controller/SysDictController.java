package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysDict;
import cqie.edu.ems.domain.qo.SysDictQo;
import cqie.edu.ems.domain.vo.SysDictVo;
import cqie.edu.ems.service.SysDictService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @PostMapping("/paged")
    public Result<PageInfo<SysDictVo>> paged(@RequestBody PageQo<SysDictQo> qo) {
        return Result.success(sysDictService.paged(qo));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysDict dict) {
        if (dict.getId() == null) sysDictService.insert(dict);
        else sysDictService.update(dict);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        sysDictService.deleteById(id);
        return Result.success(null);
    }
}