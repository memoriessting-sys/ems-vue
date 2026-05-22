package cqie.edu.ems.controller;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.SysUserQo;
import cqie.edu.ems.domain.vo.LoginVo;
import cqie.edu.ems.domain.vo.SysUserVo;
import cqie.edu.ems.service.SysUserService;
import cqie.edu.ems.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> ADMIN_PERMISSIONS = List.of(
            "home", "emp:list", "emp:add", "emp:edit", "emp:delete",
            "dept:list", "dept:add", "dept:edit", "dept:delete",
            "dict:list", "dict:add", "dict:edit", "dict:delete",
            "postLevel:list", "postLevel:add", "postLevel:edit", "postLevel:delete",
            "attendance:list", "attendance:edit",
            "salary:list", "salary:add", "salary:edit", "salary:pay",
            "leave:list", "leave:add", "leave:approve", "leave:return",
            "chart:view",
            "user:list", "user:add", "user:edit", "user:delete", "user:resetPwd"
    );

    private static final List<String> MANAGER_PERMISSIONS = List.of(
            "home", "emp:list", "emp:add", "emp:edit",
            "dept:list",
            "postLevel:list",
            "attendance:list", "attendance:edit",
            "salary:list", "salary:add", "salary:edit", "salary:pay",
            "leave:list", "leave:add", "leave:approve", "leave:return",
            "chart:view"
    );

    private static final List<String> EMPLOYEE_PERMISSIONS = List.of(
            "home",
            "attendance:list",
            "salary:list",
            "leave:list", "leave:add"
    );

    private List<String> getPermissions(Integer roleType) {
        return switch (roleType) {
            case 3 -> ADMIN_PERMISSIONS;
            case 2 -> MANAGER_PERMISSIONS;
            default -> EMPLOYEE_PERMISSIONS;
        };
    }

    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody SysUser user) {
        SysUser loginUser = sysUserService.checkLogin(user.getAccount(), user.getPassword());
        if (loginUser == null) return Result.error("账号或密码错误");
        if (loginUser.getStatus() != 0) return Result.error("该账号被禁用");

        String token = jwtUtil.generateToken(loginUser.getId(), loginUser.getAccount(), loginUser.getName(), loginUser.getRoleType());
        LoginVo vo = new LoginVo();
        vo.setToken(token);
        vo.setUserId(loginUser.getId());
        vo.setAccount(loginUser.getAccount());
        vo.setName(loginUser.getName());
        vo.setRoleType(loginUser.getRoleType());
        vo.setPermissions(getPermissions(loginUser.getRoleType()));
        return Result.success(vo);
    }

    @GetMapping("/info")
    public Result<LoginVo> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Integer roleType = (Integer) request.getAttribute("roleType");
        String account = (String) request.getAttribute("account");
        String name = (String) request.getAttribute("userName");

        LoginVo vo = new LoginVo();
        vo.setUserId(userId);
        vo.setAccount(account);
        vo.setName(name);
        vo.setRoleType(roleType);
        vo.setPermissions(getPermissions(roleType));
        return Result.success(vo);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success(null);
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
