package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.EmpPostLevel;
import cqie.edu.ems.domain.qo.EmpPostLevelQo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpPostLevelMapper {
    List<EmpPostLevel> list(EmpPostLevelQo qo);
    // List<EmpPostLevel> getAllEmpPostLevel(); // 获取所有岗位

    // 新增实体
    int insert(EmpPostLevel mo);

    // 修改实体
    int update(EmpPostLevel mo);

    // 根据主键删除实体
    int deleteById(Long id);

    // 根据主键获取实体
    EmpPostLevel getById(Long id);

    // 根据name获取实体，用于判断name是否存在，以控制name不允许重复
    EmpPostLevel getByName(@Param("name") String account, @Param("id") Long id);
}
