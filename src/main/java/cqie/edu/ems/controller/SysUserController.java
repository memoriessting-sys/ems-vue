package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.SysUserQo;
import cqie.edu.ems.domain.vo.SysUserVo;
import cqie.edu.ems.service.SysUserService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody SysUser user, HttpSession session) {
        SysUser loginUser = sysUserService.checkLogin(user.getAccount(), user.getPassword());
        if (loginUser == null) return Result.error("账号或密码错误");
        if (loginUser.getStatus() != 0) return Result.error("该账号被禁用");
        session.setAttribute("loginUser", loginUser);
        return Result.success(loginUser);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success(null);
    }

    @GetMapping("/info")
    public Result<SysUser> info(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) return Result.error("未登录");
        return Result.success(user);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody SysUser user) {
        if (sysUserService.existAccount(user.getAccount(), null)) return Result.error("登录账号已存在");
        user.setStatus(0);
        user.setRoleType(1);
        sysUserService.insert(user);
        return Result.success(null);
    }

    @PostMapping("/forgotPassword")
    public Result<Void> forgotPassword(@RequestBody SysUser user) {
        SysUser existing = sysUserService.getByAccount(user.getAccount());
        if (existing == null) return Result.error("账户不存在");
        existing.setPassword(user.getPassword());
        sysUserService.update(existing);
        return Result.success(null);
    }

    @PostMapping("/paged")
    public Result<PageInfo<SysUserVo>> paged(@RequestBody PageQo<SysUserQo> qo) {
        return Result.success(sysUserService.paged(qo));
    }

    @GetMapping("/{id}")
    public Result<SysUserVo> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysUser user) {
        if (user.getId() == null) sysUserService.insert(user);
        else sysUserService.update(user);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.deleteById(id);
        return Result.success(null);
    }

    @PutMapping("/resetPwd/{id}")
    public Result<Void> resetPwd(@PathVariable Long id) {
        sysUserService.resetPwd(id);
        return Result.success(null);
    }
}