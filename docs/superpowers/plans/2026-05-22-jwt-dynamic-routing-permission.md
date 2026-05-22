# JWT认证 + 动态路由 + 按钮权限 实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将Session认证改为JWT，前端改为动态路由+按钮权限控制

**Architecture:** 后端JJWT生成/验证JWT token，JwtInterceptor拦截请求；前端登录后获取权限列表，动态生成路由和菜单，v-permission指令控制按钮可见性

**Tech Stack:** JJWT 0.12.5, Spring MVC Interceptor, Vue3 Router addRoute, Pinia, Vue3 Custom Directive

---

## File Structure

### 新增文件
| 文件 | 职责 |
|------|------|
| `src/main/java/cqie/edu/ems/util/JwtUtil.java` | JWT生成/解析/验证工具类 |
| `src/main/java/cqie/edu/ems/interceptor/JwtInterceptor.java` | JWT拦截器，验证token并注入用户信息 |
| `src/main/java/cqie/edu/ems/domain/vo/LoginVo.java` | 登录响应VO（token+用户信息+权限） |
| `ems-vue/src/router/routes.js` | 全部业务路由定义（含meta.permissions） |
| `ems-vue/src/router/guard.js` | 导航守卫逻辑 |
| `ems-vue/src/store/permission.js` | 动态路由状态管理 |
| `ems-vue/src/directive/permission.js` | v-permission自定义指令 |

### 修改文件
| 文件 | 修改内容 |
|------|---------|
| `pom.xml` | 添加JJWT依赖 |
| `src/main/resources/application.yml` | 添加jwt配置 |
| `src/main/java/cqie/edu/ems/config/WebMvcConfig.java` | 注册JwtInterceptor |
| `src/main/java/cqie/edu/ems/controller/SysUserController.java` | login返回JWT；新增info接口；移除HttpSession |
| `src/main/java/cqie/edu/ems/controller/EmpLeaveRecordController.java` | HttpSession→request属性 |
| `src/main/java/cqie/edu/ems/controller/EmpAttendanceRecordController.java` | HttpSession→request属性 |
| `src/main/java/cqie/edu/ems/controller/EmpSalaryController.java` | HttpSession→request属性 |
| `src/main/java/cqie/edu/ems/controller/ChartController.java` | HttpSession→request属性 |
| `ems-vue/src/api/request.js` | 请求拦截器注入token，401响应处理 |
| `ems-vue/src/api/user.js` | 新增fetchUser api |
| `ems-vue/src/store/user.js` | 改用token+权限列表，移除session依赖 |
| `ems-vue/src/router/index.js` | 仅保留静态路由，引入guard |
| `ems-vue/src/layout/Layout.vue` | 侧边栏从动态路由生成 |
| `ems-vue/src/main.js` | 注册v-permission指令 |
| `ems-vue/src/views/emp/EmpList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/dept/DeptList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/dict/DictList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/postLevel/PostLevelList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/attendance/AttendanceList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/salary/SalaryList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/leave/LeaveList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/user/UserList.vue` | 按钮添加v-permission |
| `ems-vue/src/views/login/Login.vue` | 登录后跳转逻辑适配动态路由 |

---

### Task 1: 后端 - 添加JJWT依赖和JWT配置

**Files:**
- Modify: `pom.xml`
- Modify: `src/main/resources/application.yml`

- [ ] **Step 1: 在pom.xml中添加JJWT依赖**

在 `pom.xml` 的 `<dependencies>` 中添加：

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
```

- [ ] **Step 2: 在application.yml中添加jwt配置**

在 `application.yml` 末尾添加：

```yaml
jwt:
  secret: ems-jwt-secret-key-2024-must-be-at-least-256-bits-long-for-hs256
  expiration: 7200000
```

- [ ] **Step 3: Commit**

```bash
git add pom.xml src/main/resources/application.yml
git commit -m "feat: 添加JJWT依赖和JWT配置"
```

---

### Task 2: 后端 - JwtUtil工具类

**Files:**
- Create: `src/main/java/cqie/edu/ems/util/JwtUtil.java`

- [ ] **Step 1: 创建JwtUtil类**

```java
package cqie.edu.ems.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String account, String name, Integer roleType) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("account", account)
                .claim("name", name)
                .claim("roleType", roleType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public Integer getRoleTypeFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("roleType", Integer.class);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/cqie/edu/ems/util/JwtUtil.java
git commit -m "feat: 添加JwtUtil工具类"
```

---

### Task 3: 后端 - JwtInterceptor拦截器

**Files:**
- Create: `src/main/java/cqie/edu/ems/interceptor/JwtInterceptor.java`
- Modify: `src/main/java/cqie/edu/ems/config/WebMvcConfig.java`

- [ ] **Step 1: 创建JwtInterceptor**

```java
package cqie.edu.ems.interceptor;

import cqie.edu.ems.common.Result;
import cqie.edu.ems.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "未登录");
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            sendUnauthorized(response, "token已过期或无效");
            return false;
        }

        // 将用户信息注入request属性
        var claims = jwtUtil.parseToken(token);
        request.setAttribute("userId", Long.parseLong(claims.getSubject()));
        request.setAttribute("roleType", claims.get("roleType", Integer.class));
        request.setAttribute("account", claims.get("account", String.class));
        request.setAttribute("userName", claims.get("name", String.class));
        return true;
    }

    private void sendUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(msg)));
    }
}
```

- [ ] **Step 2: 修改WebMvcConfig注册拦截器**

读取当前 `WebMvcConfig.java` 内容，将其修改为：

```java
package cqie.edu.ems.config;

import cqie.edu.ems.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/user/forgotPassword"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

- [ ] **Step 3: 删除独立的CorsConfig.java（如存在）**

如果 `src/main/java/cqie/edu/ems/config/CorsConfig.java` 存在且与WebMvcConfig中的CORS配置重复，删除它。

- [ ] **Step 4: Commit**

```bash
git add src/main/java/cqie/edu/ems/interceptor/JwtInterceptor.java src/main/java/cqie/edu/ems/config/WebMvcConfig.java
git commit -m "feat: 添加JwtInterceptor并注册到WebMvcConfig"
```

---

### Task 4: 后端 - LoginVo和权限映射

**Files:**
- Create: `src/main/java/cqie/edu/ems/domain/vo/LoginVo.java`

- [ ] **Step 1: 创建LoginVo**

```java
package cqie.edu.ems.domain.vo;

import lombok.Data;
import java.util.List;

@Data
public class LoginVo {
    private String token;
    private Long userId;
    private String account;
    private String name;
    private Integer roleType;
    private List<String> permissions;
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/cqie/edu/ems/domain/vo/LoginVo.java
git commit -m "feat: 添加LoginVo（含token和权限列表）"
```

---

### Task 5: 后端 - 修改SysUserController

**Files:**
- Modify: `src/main/java/cqie/edu/ems/controller/SysUserController.java`

- [ ] **Step 1: 修改SysUserController**

重写整个Controller，关键改动：
1. 移除所有 `HttpSession` 参数
2. login方法：验证通过后用JwtUtil生成token，返回LoginVo
3. 新增info方法：从request属性获取userId，查询用户信息+权限返回
4. logout方法：改为空操作（前端清除token）
5. 新增私有方法getPermissions：按roleType返回权限标识列表

```java
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
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/cqie/edu/ems/controller/SysUserController.java
git commit -m "feat: SysUserController改用JWT认证，新增info接口和权限映射"
```

---

### Task 6: 后端 - 其他Controller移除HttpSession

**Files:**
- Modify: `src/main/java/cqie/edu/ems/controller/EmpLeaveRecordController.java`
- Modify: `src/main/java/cqie/edu/ems/controller/EmpAttendanceRecordController.java`
- Modify: `src/main/java/cqie/edu/ems/controller/EmpSalaryController.java`
- Modify: `src/main/java/cqie/edu/ems/controller/ChartController.java`

- [ ] **Step 1: 修改EmpLeaveRecordController**

将所有 `HttpSession session` 参数替换为 `HttpServletRequest request`，将 `session.getAttribute("loginUser")` 替换为 `request.getAttribute("userId")`。

具体改动点：
- 方法签名：移除 `HttpSession session`，添加 `HttpServletRequest request`
- 获取当前用户ID：`Long userId = (Long) request.getAttribute("userId")`
- 原来通过 `loginUser.getId()` 获取的地方改为直接用 `userId`
- 需要用户名的地方用 `(String) request.getAttribute("userName")`

- [ ] **Step 2: 修改EmpAttendanceRecordController**

同样模式：HttpSession → HttpServletRequest，session.getAttribute("loginUser") → request.getAttribute("userId")

- [ ] **Step 3: 修改EmpSalaryController**

同样模式：HttpSession → HttpServletRequest

- [ ] **Step 4: 修改ChartController**

同样模式：HttpSession → HttpServletRequest

- [ ] **Step 5: Commit**

```bash
git add src/main/java/cqie/edu/ems/controller/
git commit -m "refactor: 所有Controller从HttpSession迁移到JWT request属性"
```

---

### Task 7: 前端 - request.js添加token拦截器

**Files:**
- Modify: `ems-vue/src/api/request.js`

- [ ] **Step 1: 修改request.js**

在axios实例上添加请求拦截器（注入Authorization头）和响应拦截器（处理401）。

```javascript
import axios from 'axios'
import {ElMessage} from 'element-plus'
import router from '../router'

const request = axios.create({
    baseURL: '/ems/api',
    timeout: 10000
})

// 请求拦截器：注入token
request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, error => {
    return Promise.reject(error)
})

// 响应拦截器：处理401和业务错误
request.interceptors.response.use(response => {
    const res = response.data
    if (res.code === 200) {
        return res
    } else {
        ElMessage.error(res.msg || '请求失败')
        return Promise.reject(new Error(res.msg || '请求失败'))
    }
}, error => {
    if (error.response && error.response.status === 401) {
        localStorage.removeItem('token')
        router.push('/login')
        ElMessage.error('登录已过期，请重新登录')
    } else {
        ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
})

export default request
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/api/request.js
git commit -m "feat: request.js添加token请求拦截器和401响应处理"
```

---

### Task 8: 前端 - user.js API适配

**Files:**
- Modify: `ems-vue/src/api/user.js`

- [ ] **Step 1: 修改user.js**

添加fetchUser接口，适配新的login返回格式。

```javascript
import request from './request'

export function login(data) {
    return request.post('/user/login', data)
}

export function fetchUser() {
    return request.get('/user/info')
}

export function logout() {
    return request.post('/user/logout')
}

export function saveUser(data) {
    return request.post('/user/save', data)
}

export function deleteUser(id) {
    return request.post(`/user/delete/${id}`)
}

export function resetPwd(id) {
    return request.post(`/user/resetPwd/${id}`)
}

export function queryUser(data) {
    return request.post('/user/query', data)
}

export function forgotPassword(account, email) {
    return request.get('/user/forgotPassword', {params: {account, email}})
}
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/api/user.js
git commit -m "feat: user.js添加fetchUser接口"
```

---

### Task 9: 前端 - user store改造

**Files:**
- Modify: `ems-vue/src/store/user.js`

- [ ] **Step 1: 重写user.js store**

```javascript
import {defineStore} from 'pinia'
import {login as loginApi, fetchUser as fetchUserApi, logout as logoutApi} from '../api/user'
import router from '../router'

export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        userId: null,
        account: '',
        name: '',
        roleType: null,
        permissions: []
    }),
    getters: {
        isLoggedIn: (state) => !!state.token,
        isAdmin: (state) => state.roleType === 3
    },
    actions: {
        async login(loginForm) {
            const res = await loginApi(loginForm)
            const data = res.data
            this.token = data.token
            this.userId = data.userId
            this.account = data.account
            this.name = data.name
            this.roleType = data.roleType
            this.permissions = data.permissions
            localStorage.setItem('token', data.token)
        },
        async fetchUser() {
            const res = await fetchUserApi()
            const data = res.data
            this.userId = data.userId
            this.account = data.account
            this.name = data.name
            this.roleType = data.roleType
            this.permissions = data.permissions
        },
        async logout() {
            try {
                await logoutApi()
            } catch (e) {
                // 忽略logout接口错误
            }
            this.token = ''
            this.userId = null
            this.account = ''
            this.name = ''
            this.roleType = null
            this.permissions = []
            localStorage.removeItem('token')
            router.push('/login')
        },
        hasPermission(perm) {
            return this.permissions.includes(perm)
        }
    }
})
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/store/user.js
git commit -m "feat: user store改用token+权限列表"
```

---

### Task 10: 前端 - permission store和业务路由定义

**Files:**
- Create: `ems-vue/src/store/permission.js`
- Create: `ems-vue/src/router/routes.js`

- [ ] **Step 1: 创建permission store**

```javascript
import {defineStore} from 'pinia'

export const usePermissionStore = defineStore('permission', {
    state: () => ({
        routes: [],
        isRoutesLoaded: false
    }),
    actions: {
        generateRoutes(businessRoutes, permissions) {
            const filtered = filterRoutes(businessRoutes, permissions)
            this.routes = filtered
            this.isRoutesLoaded = true
            return filtered
        },
        resetRoutes() {
            this.routes = []
            this.isRoutesLoaded = false
        }
    }
})

function filterRoutes(routes, permissions) {
    return routes.filter(route => {
        const required = route.meta?.permissions || []
        if (required.length === 0) return true
        const hasAccess = required.some(p => permissions.includes(p))
        if (hasAccess && route.children) {
            route.children = filterRoutes(route.children, permissions)
        }
        return hasAccess
    })
}
```

- [ ] **Step 2: 创建业务路由定义 routes.js**

```javascript
import Layout from '../layout/Layout.vue'

const Home = () => import('../views/home/Home.vue')
const EmpList = () => import('../views/emp/EmpList.vue')
const DeptList = () => import('../views/dept/DeptList.vue')
const DictList = () => import('../views/dict/DictList.vue')
const PostLevelList = () => import('../views/postLevel/PostLevelList.vue')
const AttendanceList = () => import('../views/attendance/AttendanceList.vue')
const SalaryList = () => import('../views/salary/SalaryList.vue')
const LeaveList = () => import('../views/leave/LeaveList.vue')
const Chart = () => import('../views/chart/Chart.vue')
const UserList = () => import('../views/user/UserList.vue')

export const businessRoutes = [
    {
        path: '/',
        component: Layout,
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'Home',
                component: Home,
                meta: {title: '首页概览', icon: 'HomeFilled', permissions: ['home']}
            },
            {
                path: 'emp',
                name: 'Emp',
                component: EmpList,
                meta: {title: '员工管理', icon: 'User', permissions: ['emp:list']}
            },
            {
                path: 'dept',
                name: 'Dept',
                component: DeptList,
                meta: {title: '部门管理', icon: 'OfficeBuilding', permissions: ['dept:list']}
            },
            {
                path: 'dict',
                name: 'Dict',
                component: DictList,
                meta: {title: '字典管理', icon: 'Collection', permissions: ['dict:list']}
            },
            {
                path: 'postLevel',
                name: 'PostLevel',
                component: PostLevelList,
                meta: {title: '岗位等级', icon: 'Stamp', permissions: ['postLevel:list']}
            },
            {
                path: 'attendance',
                name: 'Attendance',
                component: AttendanceList,
                meta: {title: '考勤管理', icon: 'Calendar', permissions: ['attendance:list']}
            },
            {
                path: 'salary',
                name: 'Salary',
                component: SalaryList,
                meta: {title: '薪资管理', icon: 'Money', permissions: ['salary:list']}
            },
            {
                path: 'leave',
                name: 'Leave',
                component: LeaveList,
                meta: {title: '请假管理', icon: 'Clock', permissions: ['leave:list']}
            },
            {
                path: 'chart',
                name: 'Chart',
                component: Chart,
                meta: {title: '数据统计', icon: 'DataAnalysis', permissions: ['chart:view']}
            },
            {
                path: 'user',
                name: 'User',
                component: UserList,
                meta: {title: '用户管理', icon: 'Setting', permissions: ['user:list']}
            }
        ]
    }
]
```

- [ ] **Step 3: Commit**

```bash
git add ems-vue/src/store/permission.js ems-vue/src/router/routes.js
git commit -m "feat: 添加permission store和业务路由定义"
```

---

### Task 11: 前端 - 导航守卫guard.js

**Files:**
- Create: `ems-vue/src/router/guard.js`

- [ ] **Step 1: 创建导航守卫**

```javascript
import {useUserStore} from '../store/user'
import {usePermissionStore} from '../store/permission'
import {businessRoutes} from './routes'
import router from './index'

const whiteList = ['/login', '/forgot-password']

router.beforeEach(async (to, from, next) => {
    const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    if (userStore.isLoggedIn) {
        if (to.path === '/login') {
            next({path: '/'})
        } else {
            if (permissionStore.isRoutesLoaded) {
                next()
            } else {
                try {
                    // 获取用户信息和权限
                    await userStore.fetchUser()
                    // 根据权限过滤路由
                    const accessRoutes = permissionStore.generateRoutes(businessRoutes, userStore.permissions)
                    // 动态添加路由
                    accessRoutes.forEach(route => {
                        router.addRoute(route)
                    })
                    // 重新导航到目标页（确保动态路由已生效）
                    next({...to, replace: true})
                } catch (error) {
                    // 获取用户信息失败，清除token跳转登录
                    await userStore.logout()
                    next(`/login?redirect=${to.path}`)
                }
            }
        }
    } else {
        if (whiteList.includes(to.path)) {
            next()
        } else {
            next(`/login?redirect=${to.path}`)
        }
    }
})
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/router/guard.js
git commit -m "feat: 添加导航守卫（动态路由生成逻辑）"
```

---

### Task 12: 前端 - router/index.js改造

**Files:**
- Modify: `ems-vue/src/router/index.js`

- [ ] **Step 1: 重写router/index.js**

仅保留静态路由（login、404等），引入guard。

```javascript
import {createRouter, createWebHistory} from 'vue-router'

const Login = () => import('../views/login/Login.vue')

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {title: '登录'}
    }
]

const router = createRouter({
    history: createWebHistory('/ems/'),
    routes
})

// 引入导航守卫
import './guard'

export default router
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/router/index.js
git commit -m "feat: router/index.js仅保留静态路由，引入guard"
```

---

### Task 13: 前端 - v-permission指令

**Files:**
- Create: `ems-vue/src/directive/permission.js`

- [ ] **Step 1: 创建v-permission指令**

```javascript
import {useUserStore} from '../store/user'

export default {
    mounted(el, binding) {
        const userStore = useUserStore()
        const required = binding.value
        if (!required) return
        if (!userStore.permissions.includes(required)) {
            el.parentNode && el.parentNode.removeChild(el)
        }
    }
}
```

- [ ] **Step 2: 在main.js中注册指令**

在main.js中添加：

```javascript
import permissionDirective from './directive/permission'

const app = createApp(App)
// ... 其他注册
app.directive('permission', permissionDirective)
```

- [ ] **Step 3: Commit**

```bash
git add ems-vue/src/directive/permission.js ems-vue/src/main.js
git commit -m "feat: 添加v-permission指令并在main.js全局注册"
```

---

### Task 14: 前端 - Layout侧边栏改造

**Files:**
- Modify: `ems-vue/src/layout/Layout.vue`

- [ ] **Step 1: 修改Layout.vue侧边栏为动态路由驱动**

读取当前Layout.vue内容，将硬编码的侧边栏菜单改为从permission store的routes动态生成。

关键改动：
- 移除所有硬编码的 `<el-menu-item>` 和 `<el-sub-menu>`
- 从 `usePermissionStore().routes[0].children` 获取菜单数据
- 用 `v-for` 循环渲染菜单项
- 每个菜单项的 `index` 用 `route.path`，图标用 `route.meta.icon`，标题用 `route.meta.title`
- 移除 `isAdmin` 相关的 `v-if` 判断

template中侧边栏部分改为：

```html
<el-menu :default-active="activeMenu" router background-color="#304156"
         text-color="#bfcbd9" active-text-color="#409EFF">
  <template v-for="route in menuRoutes" :key="route.path">
    <el-menu-item :index="'/' + route.path">
      <el-icon><component :is="route.meta.icon"/></el-icon>
      <span>{{ route.meta.title }}</span>
    </el-menu-item>
  </template>
</el-menu>
```

script中添加：

```javascript
import {usePermissionStore} from '../store/permission'
import {computed} from 'vue'
import {useRoute} from 'vue-router'

const permissionStore = usePermissionStore()
const route = useRoute()
const menuRoutes = computed(() => permissionStore.routes[0]?.children || [])
const activeMenu = computed(() => route.path)
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/layout/Layout.vue
git commit -m "feat: Layout侧边栏改为动态路由驱动"
```

---

### Task 15: 前端 - Login.vue适配

**Files:**
- Modify: `ems-vue/src/views/login/Login.vue`

- [ ] **Step 1: 修改Login.vue的登录逻辑**

关键改动：
- 登录成功后不再直接 `router.push('/home')`，而是 `router.push(redirect || '/')`，让导航守卫处理动态路由生成
- 从 `userStore.login()` 获取数据，store内部处理token存储
- 移除对session的任何引用

```javascript
const handleLogin = async () => {
    if (!loginFormRef.value) return
    await loginFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                await userStore.login(loginForm)
                const redirect = route.query.redirect || '/'
                router.push(redirect)
            } catch (e) {
                // 错误已在拦截器中处理
            }
        }
    })
}
```

- [ ] **Step 2: Commit**

```bash
git add ems-vue/src/views/login/Login.vue
git commit -m "feat: Login.vue适配JWT登录和动态路由跳转"
```

---

### Task 16: 前端 - 所有页面添加v-permission

**Files:**
- Modify: `ems-vue/src/views/emp/EmpList.vue`
- Modify: `ems-vue/src/views/dept/DeptList.vue`
- Modify: `ems-vue/src/views/dict/DictList.vue`
- Modify: `ems-vue/src/views/postLevel/PostLevelList.vue`
- Modify: `ems-vue/src/views/attendance/AttendanceList.vue`
- Modify: `ems-vue/src/views/salary/SalaryList.vue`
- Modify: `ems-vue/src/views/leave/LeaveList.vue`
- Modify: `ems-vue/src/views/user/UserList.vue`

- [ ] **Step 1: EmpList.vue添加v-permission**

在新增、编辑、删除按钮上添加：
- `v-permission="'emp:add'"` — 新增按钮
- `v-permission="'emp:edit'"` — 编辑按钮
- `v-permission="'emp:delete'"` — 删除按钮

- [ ] **Step 2: DeptList.vue添加v-permission**

- `v-permission="'dept:add'"` — 新增
- `v-permission="'dept:edit'"` — 编辑
- `v-permission="'dept:delete'"` — 删除

- [ ] **Step 3: DictList.vue添加v-permission**

- `v-permission="'dict:add'"` — 新增
- `v-permission="'dict:edit'"` — 编辑
- `v-permission="'dict:delete'"` — 删除

- [ ] **Step 4: PostLevelList.vue添加v-permission**

- `v-permission="'postLevel:add'"` — 新增
- `v-permission="'postLevel:edit'"` — 编辑
- `v-permission="'postLevel:delete'"` — 删除

- [ ] **Step 5: AttendanceList.vue添加v-permission**

- `v-permission="'attendance:edit'"` — 编辑/签到签退按钮

- [ ] **Step 6: SalaryList.vue添加v-permission**

- `v-permission="'salary:add'"` — 新增
- `v-permission="'salary:edit'"` — 编辑
- `v-permission="'salary:pay'"` — 批量发放

- [ ] **Step 7: LeaveList.vue添加v-permission**

- `v-permission="'leave:add'"` — 新增请假
- `v-permission="'leave:approve'"` — 审批按钮
- `v-permission="'leave:return'"` — 销假按钮

- [ ] **Step 8: UserList.vue添加v-permission**

- `v-permission="'user:add'"` — 新增
- `v-permission="'user:edit'"` — 编辑
- `v-permission="'user:delete'"` — 删除
- `v-permission="'user:resetPwd'"` — 重置密码

- [ ] **Step 9: Commit**

```bash
git add ems-vue/src/views/
git commit -m "feat: 所有页面添加v-permission按钮权限控制"
```

---

### Task 17: 后端编译验证和前端启动验证

- [ ] **Step 1: 后端编译**

```bash
mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 2: 前端编译**

```bash
cd ems-vue && npm run build
```

Expected: 构建成功，无错误

- [ ] **Step 3: 启动后端和前端进行手动测试**

```bash
# 终端1
mvn spring-boot:run

# 终端2
cd ems-vue && npm run dev
```

测试场景：
1. 未登录访问任何页面 → 跳转login
2. 管理员登录 → 看到全部菜单，全部按钮
3. 主管登录 → 看到部分菜单（无字典、用户管理），有对应按钮
4. 员工登录 → 仅看到首页、考勤、薪资、请假菜单，无增删改按钮
5. 修改token后请求 → 401跳转login

- [ ] **Step 4: 最终Commit**

```bash
git add -A
git commit -m "feat: JWT认证+动态路由+按钮权限改造完成"
```
