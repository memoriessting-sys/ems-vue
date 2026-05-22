package cqie.edu.ems.mapper;

import cqie.edu.ems.domain.entity.EmpAttendanceRecord;
import cqie.edu.ems.domain.qo.EmpAttendanceRecordQo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface EmpAttendanceRecordMapper {
    // 查询符合条件的全部实体
    List<EmpAttendanceRecord> list(EmpAttendanceRecordQo qo);

    // 新增实体
    int insert(EmpAttendanceRecord mo);

    // 修改实体
    int update(EmpAttendanceRecord mo);

    // 根据主键删除实体
    int deleteById(Long id);

    // 根据主键获取实体
    EmpAttendanceRecord getById(Long id);

    // 根据用户ID和考勤日期查询考勤记录
    EmpAttendanceRecord getByUserIdAndDate(@Param("userId") Long userId,
            @Param("attendanceDate") LocalDate attendanceDate);

    // 获取今日考勤总数
    int countTodayAttendance(@Param("today") LocalDate today);

    // 根据状态统计考勤记录数量
    int countByState(@Param("state") Integer state);

    /**
     * 获取考勤趋势统计
     */
    List<Map<String, Object>> getAttendanceTrend(@Param("days") int days);
}