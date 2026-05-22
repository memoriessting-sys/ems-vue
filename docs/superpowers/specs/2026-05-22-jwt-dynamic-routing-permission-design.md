# JWT认证 + 动态路由 + 按钮权限 设计文档

日期: 2026-05-22

## 目标

将当前基于HttpSession的认证改为JWT，前端改为动态路由（根据权限生成菜单），并实现v-permission指令控制按钮级权限。

## 方案选择

采用方案B（代码驱动）：前端定义菜单/路由配置+权限标识，后端返回用户角色和权限列表，前端根据权限过滤生成动态路由。无需新增数据库表。

## 第1部分：后端JWT认证

### 新增依赖（pom.xml）

```xml
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

### 新增配置（application.yml）

```yaml
jwt:
  secret: ems-jwt-secret-key-2024-must-be-at-least-256-bits-long-for-hs256
  expiration: 7200000  # 2小时，单位毫秒
```

### 新增类

#### JwtUtil（cqie.edu.ems.util.JwtUtil）

- `generateToken(Long userId, String account, String name, Integer roleType)` → 生成JWT
- `parseToken(String token)` → 解析JWT返回Claims
- `getUserIdFromToken(String token)` → 从token获取userId
- `validateToken(String token)` → 验证token有效性
- Token内容：sub=userId, account, name, roleType, iat, exp

#### JwtInterceptor（cqie.edu.ems.interceptor.JwtInterceptor）

- 实现 `HandlerInterceptor`
- 从 `Authorization: Bearer <token>` 获取token
- 解析token，将userId和roleType存入request属性：`request.setAttribute("userId", ...)`、`request.setAttribute("roleType", ...)`
- 无效token返回401 JSON响应 `Result.error("未登录或token已过期")`

#### 修改 WebMvcConfig

- 注册JwtInterceptor，拦截 `/api/**`
- 排除路径：`/api/user/login`、`/api/user/register`、`/api/user/forgotPassword`

### 修改类

#### SysUserController

- `login`：验证通过后生成JWT返回 `Result.success(Map.of("token", token, "user", userVo))`
- `logout`：改为空操作（JWT无状态，前端清除token即可）
- `info`：从request属性获取userId，查询最新用户信息返回，同时返回permissions列表
- 新增 `getPermissions(Integer roleType)` 私有方法，按角色返回权限标识列表

#### 所有使用HttpSession的Controller

- `EmpLeaveRecordController`：`session.getAttribute("loginUser")` → `request.getAttribute("userId")`
- `EmpAttendanceRecordController`：同上
- `EmpSalaryController`：同上
- `ChartController`：同上

#### 权限映射（硬编码在Controller或Service中）

```java
// roleType=3 管理员：全部权限
// roleType=2 主管：首页、员工、部门、考勤、薪资、请假、数据统计、岗位等级
// roleType=1 员工：首页、考勤(仅自己)、请假(仅自己)、薪资(仅自己)
```

## 第2部分：前端动态路由

### 文件结构变更

```
ems-vue/src/
├── router/
│   ├── index.js        # 创建router实例，仅含静态路由(login/404)
│   ├── routes.js       # 全部业务路由定义，含meta.permissions
│   └── guard.js        # 导航守卫逻辑
├── store/
│   ├── user.js         # token + 用户信息 + 权限列表
│   └── permission.js   # 动态路由状态
├── directive/
│   └── permission.js   # v-permission指令
└── api/
    └── request.js      # 添加请求拦截器注入token
```

### router/routes.js

定义全部业务路由，每个带 `meta: { title, icon, permissions }`：

```js
export const businessRoutes = [
  { path: 'home', component: Home, meta: { title: '首页概览', icon: 'HomeFilled', permissions: ['home'] } },
  { path: 'emp', component: EmpList, meta: { title: '员工管理', icon: 'User', permissions: ['emp:list'] } },
  { path: 'dept', component: DeptList, meta: { title: '部门管理', icon: 'OfficeBuilding', permissions: ['dept:list'] } },
  // ... 其余菜单
  { path: 'user', component: UserList, meta: { title: '用户管理', icon: 'Setting', permissions: ['user:list'] } },
]
```

### router/guard.js

导航守卫逻辑：
1. 若无token → 跳转login
2. 若有token但无动态路由 → 调用 `/api/user/info` 获取用户信息+权限 → 根据permissions过滤routes → `router.addRoute()` 动态添加 → 重定向到目标页
3. 若已有动态路由 → 正常放行

### store/user.js 改造

- 存储：token（localStorage）、user（内存）、permissions（内存）
- 登录成功：存token到localStorage
- 刷新页面：token存在时自动调用fetchUser获取用户信息+权限
- 退出：清除token、user、permissions

### store/permission.js 新增

- `routes`：已生成的动态路由列表
- `generateRoutes(permissions)`：根据权限过滤businessRoutes并存储
- `isRoutesLoaded`：标记是否已生成动态路由

### Layout.vue 改造

- 侧边栏菜单从 `permissionStore.routes` 动态渲染，不再硬编码
- 移除 `isAdmin` 判断，统一由权限控制

### api/request.js 改造

- 请求拦截器：从localStorage取token，添加 `Authorization: Bearer <token>` 头
- 响应拦截器：401状态码 → 清除token → 跳转login

## 第3部分：v-permission 按钮权限

### directive/permission.js

```js
// Vue3自定义指令
app.directive('permission', {
  mounted(el, binding) {
    const permissions = useUserStore().permissions
    const required = binding.value
    if (!permissions.includes(required)) {
      el.parentNode?.removeChild(el)
    }
  }
})
```

### 权限标识命名规范

格式：`模块:操作`

| 模块 | 权限标识 |
|------|---------|
| 首页 | home |
| 员工 | emp:list, emp:add, emp:edit, emp:delete |
| 部门 | dept:list, dept:add, dept:edit, dept:delete |
| 字典 | dict:list, dict:add, dict:edit, dict:delete |
| 岗位等级 | postLevel:list, postLevel:add, postLevel:edit, postLevel:delete |
| 考勤 | attendance:list, attendance:checkIn, attendance:checkOut, attendance:edit |
| 薪资 | salary:list, salary:add, salary:edit, salary:pay |
| 请假 | leave:list, leave:add, leave:approve, leave:return |
| 数据统计 | chart:view |
| 用户管理 | user:list, user:add, user:edit, user:delete, user:resetPwd |

### 使用方式

```html
<el-button v-permission="'emp:add'" type="primary">新增员工</el-button>
<el-button v-permission="'salary:pay'" type="success">批量发放</el-button>
<el-button v-permission="'leave:approve'" type="warning">审批</el-button>
```

### 菜单权限 vs 按钮权限

- 菜单权限（粗粒度）：控制页面是否可见，有 `salary:list` 才能看到薪资管理菜单
- 按钮权限（细粒度）：控制页面内操作按钮是否可见，有 `salary:pay` 才能看到批量发放按钮
