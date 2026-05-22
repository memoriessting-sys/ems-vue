package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.Emp;
import cqie.edu.ems.domain.qo.EmpQo;
import cqie.edu.ems.domain.vo.EmpVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EmpMapper {
    List<EmpVo> list(EmpQo qo);

    List<Emp> selectList(EmpQo qo);

    int insert(Emp mo);

    int update(Emp mo);

    int deleteById(Long id);

    Emp getById(Long id);

    Emp getByCode(@Param("code") String code, @Param("id") Long id);

    Emp getByUserId(@Param("userId") Long userId);

    /**
     * 根据员工姓名查询员工信息
     * 
     * @param name 员工姓名
     * @return 员工实体
     */
    Emp getByName(@Param("name") String name);

    int countAll();

    /**
     * 获取员工分布统计（按部门）
     */
    List<Map<String, Object>> getEmployeeDistributionByDept();
}