# 企业管理系统 (EMS)

基于 Spring Boot + Vue3 的前后端分离员工管理系统。

## 技术栈

**后端：**
- Spring Boot 3.2.5
- MyBatis + PageHelper 分页
- Druid 连接池
- MySQL 数据库
- Lombok

**前端：**
- Vue 3 + Vite 5
- Element Plus 组件库
- ECharts 图表
- Pinia 状态管理
- Vue Router 路由
- Axios 请求

## 功能模块

| 模块 | 功能 |
|------|------|
| 首页概览 | 员工/部门/考勤/请假统计，部门分布饼图，考勤趋势折线图 |
| 员工管理 | 员工CRUD，按姓名/部门筛选 |
| 部门管理 | 树形部门结构，CRUD |
| 字典管理 | 数据字典CRUD |
| 岗位等级 | 岗位等级CRUD，基础薪资设置 |
| 考勤管理 | 签到/签退，按部门/员工筛选 |
| 薪资管理 | 薪资CRUD，批量发放 |
| 请假管理 | 申请/审批/驳回/销假 |
| 数据统计 | 薪资/请假/考勤/部门多维度图表 |
| 用户管理 | 用户CRUD，重置密码（管理员） |

## 项目结构

```
├── src/main/java/cqie/edu/ems/
│   ├── EmsApplication.java          # 启动类
│   ├── common/Result.java           # 统一响应
│   ├── comm/PageQo.java             # 分页查询
│   ├── config/WebMvcConfig.java     # CORS配置
│   ├── controller/                  # REST控制器
│   ├── domain/
│   │   ├── entity/                  # 实体类
│   │   ├── qo/                      # 查询对象
│   │   └── vo/                      # 视图对象
│   ├── mapper/                      # MyBatis Mapper
│   └── service/                     # 业务逻辑
├── src/main/resources/
│   ├── application.yml              # 配置文件
│   ├── mapper/                      # XML映射
│   └── sql/ems_db.sql               # 数据库脚本
└── ems-vue/                         # Vue3前端
    ├── src/
    │   ├── api/                     # API接口
    │   ├── layout/                  # 布局组件
    │   ├── router/                  # 路由配置
    │   ├── store/                   # 状态管理
    │   └── views/                   # 页面组件
    └── vite.config.js               # Vite配置(含代理)
```

## 快速开始

### 环境要求

- JDK 17+
- Node.js 20+
- MySQL 8.0+
- Maven 3.8+

### 1. 初始化数据库

```sql
CREATE DATABASE ems_db DEFAULT CHARACTER SET utf8mb4;
```

导入 `src/main/resources/sql/ems_db.sql`。

### 2. 启动后端

```bash
# 修改数据库配置（如需要）
# src/main/resources/application.yml

mvn clean package -DskipTests
java -jar target/ems-1.0-SNAPSHOT.jar
```

后端运行在 http://localhost:8080/ems

### 3. 启动前端

```bash
cd ems-vue
npm install
npm run dev
```

前端运行在 http://localhost:5173，自动代理API到后端。

### 4. 访问系统

浏览器打开 http://localhost:5173

默认账号：`admin` / `123456`（管理员）

## API 格式

所有API以 `/ems/api` 为前缀，返回统一格式：

```json
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```

分页请求体格式：

```json
{
  "pageIndex": 1,
  "pageSize": 10,
  "filters": {}
}
```
