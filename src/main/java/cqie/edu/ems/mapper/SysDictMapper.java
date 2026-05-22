package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.SysDict;
import cqie.edu.ems.domain.qo.SysDictQo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictMapper {
    // 查询符合条件的全部实体
    List<SysDict> list(SysDictQo qo);

    // 新增实体
    int insert(SysDict mo);

    // 修改实体
    int update(SysDict mo);

    // 根据主键删除实体
    int deleteById(Integer id);

    // 根据主键获取实体
    SysDict getById(Long id);

    // 根据字典类型和字典项键获取字典实体
    SysDict getByTypeAndKey(@Param("dictType") String dictType, @Param("dictItemKey") String dictItemKey);
}
