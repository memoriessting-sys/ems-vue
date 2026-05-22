package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.Emp;
import cqie.edu.ems.domain.qo.EmpQo;
import cqie.edu.ems.domain.vo.EmpVo;
import cqie.edu.ems.mapper.EmpMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptService deptService;
    @Autowired
    private EmpPostLevelService empPostLevelService;
    @Autowired
    private SysUserService sysUserService;

    // 分页查询符合条件的实体
    public PageInfo<EmpVo> paged(PageQo<EmpQo> pageQo) {
        PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());

        EmpQo filters = pageQo.getFilters();
        if (filters == null) {
            filters = new EmpQo();
            pageQo.setFilters(filters);
        }

        // 处理查询条件
        // 如果传入了name属性但没传namePattern，则为其设置模糊匹配模式
        if (filters.getName() != null && !filters.getName().isEmpty() &&
                (filters.getNamePattern() == null || filters.getNamePattern().isEmpty())) {
            String namePattern = "%" + filters.getName().trim() + "%";
            filters.setNamePattern(namePattern);
            System.out.println("EmpService - 设置name模糊查询条件: " + namePattern);
        }

        // 如果传入了code属性但没传codePattern，则为其设置模糊匹配模式
        if (filters.getCode() != null && !filters.getCode().isEmpty() &&
                (filters.getCodePattern() == null || filters.getCodePattern().isEmpty())) {
            String codePattern = "%" + filters.getCode().trim() + "%";
            filters.setCodePattern(codePattern);
            System.out.println("EmpService - 设置code模糊查询条件: " + codePattern);
        }

        // 如果传入了mobile属性但没传mobilePattern，则为其设置模糊匹配模式
        if (filters.getMobile() != null && !filters.getMobile().isEmpty() &&
                (filters.getMobilePattern() == null || filters.getMobilePattern().isEmpty())) {
            filters.setMobilePattern("%" + filters.getMobile().trim() + "%");
            System.out.println("EmpService - 设置mobile模糊查询条件: " + filters.getMobilePattern());
        }

        System.out.println("EmpService - 部门ID: " + filters.getDeptId());
        System.out.println("EmpService - 岗位ID: " + filters.getPostLevelId());
        System.out.println("EmpService - 状态: " + filters.getStatus());

        System.out.println("EmpService - Final Filters before Mapper call: " + filters);

        // 打印所有过滤条件用于调试
        if (filters.getName() != null)
            System.out.println("Filter name: " + filters.getName());
        if (filters.getNamePattern() != null)
            System.out.println("Filter namePattern: " + filters.getNamePattern());
        if (filters.getCode() != null)
            System.out.println("Filter code: " + filters.getCode());
        if (filters.getCodePattern() != null)
            System.out.println("Filter codePattern: " + filters.getCodePattern());

        List<EmpVo> vos = empMapper.list(filters);

        System.out
                .println("EmpService - Results after Mapper call: " + (vos == null ? "null" : vos.size() + " records"));
        if (vos != null && vos.size() > 0) {
            System.out.println("EmpService - First result: " + vos.get(0));
        }

        if (vos == null)
            return null;

        PageInfo<EmpVo> voPageInfo = new PageInfo<>(vos);
        for (EmpVo vo : vos) {
            // 映射-绑定用户名称
            if (vo.getUserId() != null) {
                vo.setUserName(sysUserService.getById(vo.getUserId()).getName());
            }
            // 映射-性别
            if (vo.getSex() != null) {
                vo.setSexName(vo.getSex() == 1 ? "男" : (vo.getSex() == 2 ? "女" : "未知"));
            }
            // 映射-婚姻状态
            if (vo.getMaritalStatus() != null) {
                switch (vo.getMaritalStatus()) {
                    case 1:
                        vo.setMaritalStatusName("未婚");
                        break;
                    case 2:
                        vo.setMaritalStatusName("已婚");
                        break;
                    case 3:
                        vo.setMaritalStatusName("离异");
                        break;
                    default:
                        vo.setMaritalStatusName("未知");
                        break;
                }
            }
            // 映射-状态
            if (vo.getStatus() != null) {
                vo.setStatusName(vo.getStatus() == 0 ? "在职" : "离职");
            }
        }
        voPageInfo.setList(vos);
        return voPageInfo;
    }

    // 新增实体
    public int insert(Emp emp) {
        return empMapper.insert(emp);
    }

    // 修改实体
    public int update(Emp emp) {
        return empMapper.update(emp);
    }

    // 根据主键删除实体
    public int deleteById(Long id) {
        return empMapper.deleteById(id);
    }

    // 根据主键获取实体
    public Emp getById(Long id) {
        return empMapper.getById(id);
    }

    // 指定工号的实体是否已经存在
    public boolean existCode(String code, Long id) {
        Emp mo = empMapper.getByCode(code, id);
        return mo != null;
    }

    // 根据用户ID获取员工信息（用于个人信息页面）
    public EmpVo getByUserId(Long userId) {
        Emp emp = empMapper.getByUserId(userId);
        if (emp == null) {
            return null;
        }
        EmpVo vo = new EmpVo();
        BeanUtils.copyProperties(emp, vo);
        // 映射-部门名称
        if (emp.getDeptId() != null) {
            vo.setDeptName(deptService.getById(emp.getDeptId()).getName());
        }
        // 映射-岗位岗级名称
        if (emp.getPostLevelId() != null) {
            vo.setPostLevelName(empPostLevelService.getById(emp.getPostLevelId()).getName());
        }
        // 映射-性别
        if (emp.getSex() != null) {
            vo.setSexName(emp.getSex() == 1 ? "男" : (emp.getSex() == 2 ? "女" : "未知"));
        }
        // 映射-婚姻状态
        if (emp.getMaritalStatus() != null) {
            switch (emp.getMaritalStatus()) {
                case 1:
                    vo.setMaritalStatusName("未婚");
                    break;
                case 2:
                    vo.setMaritalStatusName("已婚");
                    break;
                case 3:
                    vo.setMaritalStatusName("离异");
                    break;
                default:
                    vo.setMaritalStatusName("未知");
                    break;
            }
        }
        // 映射-状态
        if (emp.getStatus() != null) {
            vo.setStatusName(emp.getStatus() == 0 ? "在职" : "离职");
        }
        return vo;
    }

    // 获取员工总数
    public int countAll() {
        return empMapper.countAll();
    }

    // 获取所有员工列表
    public List<Emp> list(EmpQo qo) {
        return empMapper.selectList(qo);
    }

    /**
     * 获取员工分布统计（按部门）
     */
    public List<Map<String, Object>> getEmployeeDistributionByDept() {
        return empMapper.getEmployeeDistributionByDept();
    }
}