package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.SysUserQo;
import cqie.edu.ems.domain.vo.SysUserVo;
import cqie.edu.ems.mapper.SysUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    // 分页查询符合条件的实体
    public PageInfo<SysUserVo> paged(PageQo<SysUserQo> pageQo) {
        PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());
        List<SysUser> mos = sysUserMapper.list(pageQo.getFilters());
        if (mos == null)
            return null;
        PageInfo<SysUser> moPageInfo = new PageInfo<>(mos);
        PageInfo<SysUserVo> voPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(moPageInfo, voPageInfo);
        List<SysUserVo> vos = new ArrayList<>();
        for (SysUser mo : mos) {
            SysUserVo vo = new SysUserVo();
            BeanUtils.copyProperties(mo, vo);
            // 映射-状态
            vo.setStatusName(vo.getStatus().equals(0) ? "正常" : "禁用");
            // 映射-角色类型
            switch (vo.getRoleType()) {
                case 1:
                    vo.setRoleTypeName("普通员工");
                    break;
                case 2:
                    vo.setRoleTypeName("主管");
                    break;
                case 3:
                    vo.setRoleTypeName("管理员");
                    break;
                default:
                    vo.setRoleTypeName("未知");
            }
            // 映射-部门名称
            vo.setDeptName(mo.getDeptName());
            vos.add(vo);
        }
        voPageInfo.setList(vos);
        return voPageInfo;
    }

    // 新增实体
    public int insert(SysUser user) {
        return sysUserMapper.insert(user);
    }

    // 修改实体
    public int update(SysUser user) {
        return sysUserMapper.update(user);
    }

    // 根据主键删除实体
    public int deleteById(long id) {
        return sysUserMapper.deleteById(id);
    }

    // 根据主键获取实体
    public SysUserVo getById(long id) {
        SysUser mo = sysUserMapper.getById(id);
        if (mo == null)
            return null;
        SysUserVo vo = new SysUserVo();
        BeanUtils.copyProperties(mo, vo);
        // 映射-状态
        vo.setStatusName(mo.getStatus().equals(0) ? "正常" : "禁用");
        return vo;
    }

    // 指定账号的实体是否已经存在
    public boolean existAccount(String account, Long id) {
        SysUser mo = sysUserMapper.getByAccount(account, id);
        return mo != null;
    }

    // 登录验证
    public SysUser checkLogin(String account, String password) {
        return sysUserMapper.checkLogin(account, password);
    }

    // 根据账号获取实体
    public SysUser getByAccount(String account) {
        return sysUserMapper.getByAccountSimple(account);
    }

    // 重置密码为默认值123456
    public void resetPwd(long id) {
        SysUser user = sysUserMapper.getById(id);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setPassword("123456");
        sysUserMapper.update(user);
    }
}
