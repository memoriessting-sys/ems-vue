# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.2.5 + Vue 3 enterprise management system (企业管理系统). Backend serves REST APIs under `/ems/api/`, frontend is a Vue 3 SPA.

## Build & Run

**Database setup:**
```sql
CREATE DATABASE ems_db DEFAULT CHARACTER SET utf8mb4;
-- Import schema from src/main/resources/sql/ems_db.sql
```

**Backend** (requires Java 17, Maven):
```bash
mvn clean package -DskipTests
java -jar target/EMS.jar
# Runs at http://localhost:8080/ems
```

**Frontend:**
```bash
cd ems-vue
npm install
npm run dev    # Dev server at http://localhost:5173, proxies /ems/api → localhost:8080
npm run build  # Production build to ems-vue/dist/
```

Default login: `admin` / `123456`

## Architecture

### Backend (src/main/java/cqie/edu/ems/)

Layered pattern: `controller` → `service` → `mapper` (MyBatis) → MySQL

- **Entity/QO/VO** in `domain.entity`, `domain.qo`, `domain.vo` — entities map to DB, QOs are query filters, VOs are response DTOs
- **Result\<T\>** in `common` — unified response wrapper (code 200/500)
- **PageQo\<Qo\>** in `comm` — pagination params (pageIndex, pageSize) + filters
- **Save pattern**: single `/save` endpoint handles insert (id==null) and update (id!=null)
- **VO mapping**: service layer uses `BeanUtils.copyProperties` + manual enrichment
- **MyBatis XML**: dynamic SQL with `<where><if>` in `src/main/resources/mapper/`
- **Config**: CORS in `config/`, Jackson date serialization

Key tables: `emp`, `emp_attendance_record`, `emp_leave_record`, `emp_salary`, `emp_post_level`, `sys_dept`, `sys_dict`, `sys_user`

### Frontend (ems-vue/src/)

- **Router** (`router/index.js`): history mode, navigation guard redirects unauthenticated users
- **Store** (`store/user.js`): Pinia, persists to localStorage
- **API** (`api/`): Axios with `baseURL: '/ems/api'`, response interceptor unwraps `res.data`
- **Layout** (`layout/Layout.vue`): sidebar + header + main content
- **Views**: 10 pages under `views/` — login, home, emp, dept, dict, postLevel, attendance, salary, leave, chart, user

## Key Conventions

- All API responses: `{ code: 200/500, msg: "...", data: ... }`
- Pagination requests: `{ pageIndex, pageSize, ...filters }` → response includes `{ list, total, pageNum, ... }`
- No test framework configured on either side
- Authentication: currently HTTP session-based (being migrated to JWT)
