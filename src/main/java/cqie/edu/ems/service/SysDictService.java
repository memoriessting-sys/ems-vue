package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.SysDict;
import cqie.edu.ems.domain.qo.SysDictQo;
import cqie.edu.ems.domain.vo.SysDictVo;
import cqie.edu.ems.mapper.SysDictMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDictService {
    @Autowired
    private SysDictMapper sysDictMapper;

    //分页查询符合条件的实体
    public PageInfo<SysDictVo> paged(PageQo<SysDictQo> pageQo) {
        PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());
        List<SysDict> mos = sysDictMapper.list(pageQo.getFilters());

        if (mos == null) return null;

        PageInfo<SysDict> moPageInfo = new PageInfo<>(mos);
        PageInfo<SysDictVo> voPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(moPageInfo, voPageInfo);
        List<SysDictVo> vos = new ArrayList<>();
        for (SysDict mo : mos) {
            SysDictVo vo = new SysDictVo();
            BeanUtils.copyProperties(mo, vo);
            //映射-状态
            vo.setStatusName(vo.getStatus().equals(0) ? "正常" : "禁用");
            vos.add(vo);
        }
        voPageInfo.setList(vos);
        return voPageInfo;
    }

    // 新增实体
    public int insert(SysDict mo) {
        return sysDictMapper.insert(mo);
    }

    // 修改实体
    public int update(SysDict mo) {
        return sysDictMapper.update(mo);
    }

    // 根据主键删除实体
    public int deleteById(Integer id) {
        return sysDictMapper.deleteById(id);
    }

    // 根据主键获取实体
    public SysDict getById(Long id) {
        return sysDictMapper.getById(id);
    }

    // 根据主键获取Vo
    public SysDictVo getVoById(Long id) {
        SysDict mo = getById(id);

        if (mo == null) return null;

        SysDictVo vo = new SysDictVo();
        BeanUtils.copyProperties(mo, vo);
        //映射-状态
        vo.setStatusName(mo.getStatus().equals(0) ? "正常" : "禁用");
        return vo;
    }
}
