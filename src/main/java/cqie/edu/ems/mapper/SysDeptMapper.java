package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.SysDept;
import cqie.edu.ems.domain.qo.SysDeptQo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDeptMapper {
    List<SysDept> list(SysDeptQo qo);

    // 根据主键获取实体
    SysDept getById(Long id);

    // 根据主键删除实体
    int deleteById(Long id);

    // 根据name获取实体，用于判断name是否存在，以控制name不允许重复
    SysDept getByName(@Param("name") String account, @Param("id") Long id);

    // 新增实体
    int insert(SysDept mo);

    // 修改实体
    int update(SysDept mo);

    Integer getParentIdById(Integer id);

    // 获取所有部门总数
    int countAll();
}
