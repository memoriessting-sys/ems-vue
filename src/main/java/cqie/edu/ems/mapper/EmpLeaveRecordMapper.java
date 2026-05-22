package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.EmpLeaveRecord;
import cqie.edu.ems.domain.qo.EmpLeaveRecordQo;
import cqie.edu.ems.domain.vo.EmpLeaveRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface EmpLeaveRecordMapper {
    // 查询符合条件的全部实体
    List<EmpLeaveRecord> list(EmpLeaveRecordQo qo);

    // 新增实体
    int insert(EmpLeaveRecord mo);

    // 修改实体
    int update(EmpLeaveRecord mo);

    // 根据主键删除实体
    int deleteById(Long id);

    // 根据主键获取实体
    EmpLeaveRecord getById(Long id);

    // 获取待审核的请假申请总数
    int countPendingLeaves();

    /**
     * 统计请假记录数量
     *
     * @param qo 查询条件
     * @return 记录数量
     */
    long count(EmpLeaveRecordQo qo);

    /**
     * 查询请假记录列表
     *
     * @param qo 查询条件
     * @return 请假记录列表
     */
    List<EmpLeaveRecordVo> selectList(EmpLeaveRecordQo qo);
}