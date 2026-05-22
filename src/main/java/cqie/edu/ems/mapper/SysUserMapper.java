package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.SysUserQo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    // 查询符合条件的全部实体
    List<SysUser> list(SysUserQo qo);

    // 新增实体
    int insert(SysUser mo);

    // 修改实体
    int update(SysUser mo);

    // 根据主键删除实体
    int deleteById(long id);

    // 根据主键获取实体
    SysUser getById(long id);

    // 根据account获取实体，用于判断account是否存在，以控制account不允许重复
    SysUser getByAccount(@Param("account") String account, @Param("id") Long id);

    // 登录验证
    SysUser checkLogin(@Param("account") String account, @Param("password") String password);

    // 根据account获取实体（简单版，只通过account查询）
    SysUser getByAccountSimple(@Param("account") String account);

    /**
     * 根据用户姓名查询用户信息
     * 
     * @param name 用户姓名
     * @return 用户实体
     */
    SysUser getByName(@Param("name") String name);
}
