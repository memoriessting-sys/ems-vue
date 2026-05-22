package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpPostLevel;
import cqie.edu.ems.domain.qo.EmpPostLevelQo;
import cqie.edu.ems.domain.vo.EmpPostLevelVo;
import cqie.edu.ems.mapper.EmpPostLevelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpPostLevelService {
    @Autowired
    private EmpPostLevelMapper empPostLevelMapper;

    // 获取所有岗位级别
    public List<EmpPostLevel> list(EmpPostLevelQo qo) {
        return empPostLevelMapper.list(qo);
    }

    // 分页查询符合条件的实体
    public PageInfo<EmpPostLevelVo> paged(PageQo<EmpPostLevelQo> pageQo) {
        PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());
        List<EmpPostLevel> mos = empPostLevelMapper.list(pageQo.getFilters());
        if (mos == null)
            return null;
        PageInfo<EmpPostLevel> moPageInfo = new PageInfo<>(mos);
        PageInfo<EmpPostLevelVo> voPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(moPageInfo, voPageInfo);
        List<EmpPostLevelVo> vos = new ArrayList<>();
        for (EmpPostLevel mo : mos) {
            EmpPostLevelVo vo = new EmpPostLevelVo();
            BeanUtils.copyProperties(mo, vo);
            // 映射-状态
            vo.setStatusName(vo.getStatus().equals(0) ? "正常" : "禁用");
            vos.add(vo);
        }
        voPageInfo.setList(vos);
        return voPageInfo;
    }

    // 新增实体
    public int insert(EmpPostLevel empPostLevel) {
        return empPostLevelMapper.insert(empPostLevel);
    }

    // 修改实体
    public int update(EmpPostLevel empPostLevel) {
        return empPostLevelMapper.update(empPostLevel);
    }

    // 根据主键删除实体
    public int deleteById(Long id) {
        return empPostLevelMapper.deleteById(id);
    }

    // 根据主键获取实体
    public EmpPostLevel getById(Long id) {
        return empPostLevelMapper.getById(id);
    }

    // 指定岗位的实体是否已经存在
    public boolean existName(String account, Long id) {
        EmpPostLevel mo = empPostLevelMapper.getByName(account, id);
        return mo != null;
    }
}
