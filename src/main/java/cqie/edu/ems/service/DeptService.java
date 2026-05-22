package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysDept;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.SysDeptQo;
import cqie.edu.ems.domain.vo.SysDeptVo;
import cqie.edu.ems.domain.vo.TreeNodeDept;
import cqie.edu.ems.mapper.SysDeptMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserService sysUserService;

    // 分页查询符合条件的实体
    public PageInfo<SysDeptVo> paged(PageQo<SysDeptQo> pageQo) {
        PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());
        List<SysDept> mos = sysDeptMapper.list(pageQo.getFilters());

        if (mos == null)
            return null;

        PageInfo<SysDept> moPageInfo = new PageInfo<>(mos);
        PageInfo<SysDeptVo> voPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(moPageInfo, voPageInfo);
        List<SysDeptVo> vos = new ArrayList<>();
        for (SysDept mo : mos) {
            SysDeptVo vo = new SysDeptVo();
            BeanUtils.copyProperties(mo, vo);
            // 映射-负责人
            if (mo.getManager_user_id() != null) {
                SysUser user = sysUserService.getById(mo.getManager_user_id());
                vo.setManagerUserName(user.getName());
            }
            // 映射-上级部门
            if (mo.getParent_id() != null) {
                SysDept dept = getById(mo.getParent_id());
                vo.setParentName(dept.getName());
            }
            // 映射-状态
            vo.setStatusName(vo.getStatus().equals(0) ? "正常" : "禁用");
            vos.add(vo);
        }
        voPageInfo.setList(vos);
        return voPageInfo;
    }

    // 新增实体
    public int insert(SysDept mo) {
        return sysDeptMapper.insert(mo);
    }

    // 修改实体
    public int update(SysDept mo) {
        return sysDeptMapper.update(mo);
    }

    // 根据主键删除实体
    public int deleteById(Long id) {
        return sysDeptMapper.deleteById(id);
    }

    // 根据主键获取实体
    public SysDept getById(Long id) {
        return sysDeptMapper.getById(id);
    }

    // 根据主键获取Vo
    public SysDeptVo getVoById(Long id) {
        SysDept mo = getById(id);

        if (mo == null)
            return null;

        SysDeptVo vo = new SysDeptVo();
        BeanUtils.copyProperties(mo, vo);
        // 映射-负责人
        if (mo.getManager_user_id() != null) {
            SysUser user = sysUserService.getById(mo.getManager_user_id());
            vo.setManagerUserName(user.getName());
        }
        // 映射-上级部门
        if (mo.getParent_id() != null) {
            SysDept dept = getById(mo.getParent_id());
            vo.setParentName(dept.getName());
        }
        // 映射-状态
        vo.setStatusName(vo.getStatus().equals(0) ? "正常" : "禁用");
        return vo;
    }

    // 上级部门备选项
    public List<TreeNodeDept> listOptionForParent(Long excludeRootId) {
        List<TreeNodeDept> ret = new ArrayList<>();
        List<SysDept> all = sysDeptMapper.list(new SysDeptQo());
        // 去掉需排除的(自己及后代)的idRange
        if (excludeRootId != null) {
            List<Long> idRange = new ArrayList<>();
            getSelfAndChildren(excludeRootId, all, idRange);
            all = all.stream().filter(x -> !idRange.contains(x.getId())).collect(Collectors.toList());
        }
        List<SysDept> rootList = all.stream()
                .filter(x -> x.getParent_id() == null || x.getParent_id() == 0L)
                .sorted(Comparator.comparing(SysDept::getId))
                .toList();
        for (SysDept item : rootList) {
            TreeNodeDept node = generateNode(item, all);
            ret.add(node);
        }
        return ret;
    }

    // 递归获取自己及后代到结果集idRange
    private void getSelfAndChildren(Long selfId, List<SysDept> all, List<Long> result) {
        result.add(selfId);
        List<SysDept> children = all.stream().filter(x -> x.getParent_id() != null && x.getParent_id().equals(selfId))
                .toList();
        for (SysDept item : children) {
            getSelfAndChildren(item.getId(), all, result);
        }
    }

    // 生成部门树的节点
    private TreeNodeDept generateNode(SysDept item, List<SysDept> all) {
        // 树节点
        TreeNodeDept node = new TreeNodeDept();
        node.setId(item.getId().toString());
        node.setText(item.getName());
        if (item.getParent_id() != null) {
            node.setPid(item.getParent_id().toString());
        }
        List<SysDept> childList = all.stream()
                .filter(x -> x.getParent_id() != null && x.getParent_id().equals(item.getId()))
                .sorted(Comparator.comparing(SysDept::getId)).toList();
        List<TreeNodeDept> children = new ArrayList<>();
        if (!childList.isEmpty()) {
            for (SysDept child : childList) {
                // 子节点
                TreeNodeDept childNode = generateNode(child, all);
                children.add(childNode);
            }
        }
        node.setChildren(children);
        return node;
    }

    // 获取所有部门总数
    public int countAll() {
        return sysDeptMapper.countAll();
    }

    // 获取所有部门列表
    public List<SysDept> getAllDepts() {
        return sysDeptMapper.list(null);
    }
}
