package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpAttendanceRecord;
import cqie.edu.ems.domain.entity.SysUser;
import cqie.edu.ems.domain.qo.EmpAttendanceRecordQo;
import cqie.edu.ems.domain.vo.EmpAttendanceRecordVo;
import cqie.edu.ems.mapper.EmpAttendanceRecordMapper;
import cqie.edu.ems.mapper.SysUserMapper;
import cqie.edu.ems.mapper.SysDeptMapper;
import cqie.edu.ems.mapper.EmpMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmpAttendanceRecordService {

    @Autowired
    private EmpAttendanceRecordMapper empAttendanceRecordMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private EmpMapper empMapper;

    // 分页查询符合条件的实体
    public PageInfo<EmpAttendanceRecordVo> paged(PageQo<EmpAttendanceRecordQo> pageQo) {
        PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());
        List<EmpAttendanceRecord> mos = empAttendanceRecordMapper.list(pageQo.getFilters());

        if (mos == null)
            return null;

        PageInfo<EmpAttendanceRecord> moPageInfo = new PageInfo<>(mos);
        PageInfo<EmpAttendanceRecordVo> voPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(moPageInfo, voPageInfo);

        List<EmpAttendanceRecordVo> vos = new ArrayList<>();
        for (EmpAttendanceRecord mo : mos) {
            EmpAttendanceRecordVo vo = new EmpAttendanceRecordVo();
            BeanUtils.copyProperties(mo, vo);

            // 确保用户名和部门名称已设置
            if (vo.getUserName() == null && mo.getUserId() != null) {
                // 尝试先从员工表获取真实姓名，如果没有则从用户表获取
                cqie.edu.ems.domain.entity.Emp emp = empMapper.getByUserId(mo.getUserId());
                if (emp != null && emp.getName() != null) {
                    vo.setUserName(emp.getName());
                } else {
                    SysUser user = sysUserMapper.getById(mo.getUserId());
                    if (user != null) {
                        vo.setUserName(user.getName());
                    }
                }
            }

            if (vo.getDepartmentName() == null && mo.getDepartmentId() != null) {
                cqie.edu.ems.domain.entity.SysDept dept = sysDeptMapper.getById(mo.getDepartmentId());
                if (dept != null) {
                    vo.setDepartmentName(dept.getName());
                }
            }

            // 映射-状态名称
            if (mo.getState() != null) {
                switch (mo.getState()) {
                    case 0:
                        vo.setStateName("正常");
                        break;
                    case 1:
                        vo.setStateName("迟到");
                        break;
                    case 2:
                        vo.setStateName("早退");
                        break;
                    case 3:
                        vo.setStateName("迟到且早退");
                        break;
                    default:
                        vo.setStateName("未知");
                }
            } else {
                vo.setStateName("未知");
            }
            vos.add(vo);
        }
        voPageInfo.setList(vos);
        return voPageInfo;
    }

    // 新增实体
    @Transactional(rollbackFor = Exception.class)
    public int insert(EmpAttendanceRecord empAttendanceRecord) {
        return empAttendanceRecordMapper.insert(empAttendanceRecord);
    }

    // 修改实体
    @Transactional(rollbackFor = Exception.class)
    public int update(EmpAttendanceRecord empAttendanceRecord) {
        return empAttendanceRecordMapper.update(empAttendanceRecord);
    }

    // 根据主键删除实体
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return empAttendanceRecordMapper.deleteById(id);
    }

    // 根据主键获取实体
    public EmpAttendanceRecord getById(Long id) {
        return empAttendanceRecordMapper.getById(id);
    }

    // 根据日期和用户ID查找考勤记录
    public EmpAttendanceRecord findByDateAndUserId(LocalDate date, Long userId) {
        return empAttendanceRecordMapper.getByUserIdAndDate(userId, date);
    }

    // 签到
    @Transactional(rollbackFor = Exception.class)
    public void checkIn(Long userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 检查今日是否已有考勤记录
        EmpAttendanceRecord existingRecord = empAttendanceRecordMapper.getByUserIdAndDate(userId, today);

        if (existingRecord != null) {
            if (existingRecord.getCheckInTime() != null) {
                throw new RuntimeException("您今天已签到，请勿重复签到！");
            }
        }

        // 判断是否迟到（假设上班时间为9:00）
        LocalTime workStartTime = LocalTime.of(9, 0);
        boolean isLate = now.isAfter(workStartTime);

        // 获取员工信息，以便获取部门ID
        cqie.edu.ems.domain.entity.Emp emp = empMapper.getByUserId(userId);
        Long departmentId = (emp != null) ? emp.getDeptId() : null;

        if (existingRecord == null) {
            // 创建新的考勤记录
            EmpAttendanceRecord record = new EmpAttendanceRecord();
            record.setUserId(userId);
            record.setAttendanceDate(today);
            record.setCheckInTime(now);
            record.setState(isLate ? 1 : 0); // 1表示迟到，0表示正常
            record.setDepartmentId(departmentId);
            empAttendanceRecordMapper.insert(record);
        } else {
            // 更新现有记录
            existingRecord.setCheckInTime(now);
            if (isLate) {
                existingRecord.setState(existingRecord.getState() == 2 ? 3 : 1); // 如果之前是早退，现在是迟到且早退
            }
            existingRecord.setDepartmentId(departmentId); // 更新部门ID，确保部门信息正确
            empAttendanceRecordMapper.update(existingRecord);
        }
    }

    // 签退
    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Long userId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 检查今日考勤记录
        EmpAttendanceRecord existingRecord = empAttendanceRecordMapper.getByUserIdAndDate(userId, today);

        if (existingRecord == null) {
            throw new RuntimeException("您今天还未签到，请先签到！");
        }

        if (existingRecord.getCheckOutTime() != null) {
            throw new RuntimeException("您今天已签退，请勿重复签退！");
        }

        // 判断是否早退（假设下班时间为18:00）
        LocalTime workEndTime = LocalTime.of(18, 0);
        boolean isEarlyLeave = now.isBefore(workEndTime);

        // 更新签退时间和状态
        existingRecord.setCheckOutTime(now);
        if (isEarlyLeave) {
            if (existingRecord.getState() == 1) {
                existingRecord.setState(3); // 迟到且早退
            } else {
                existingRecord.setState(2); // 早退
            }
        }
        // 获取员工信息，以便获取部门ID
        cqie.edu.ems.domain.entity.Emp emp = empMapper.getByUserId(userId);
        Long departmentId = (emp != null) ? emp.getDeptId() : null;
        existingRecord.setDepartmentId(departmentId); // 更新部门ID
        empAttendanceRecordMapper.update(existingRecord);
    }

    // 获取今日考勤总数
    public int countTodayAttendance() {
        LocalDate today = LocalDate.now();
        return empAttendanceRecordMapper.countTodayAttendance(today);
    }

    // 按状态统计考勤记录数量
    public int countByState(Integer state) {
        if (state == null) {
            return 0;
        }
        try {
            return empAttendanceRecordMapper.countByState(state);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取考勤趋势统计
     */
    public List<Map<String, Object>> getAttendanceTrend(int days) {
        return empAttendanceRecordMapper.getAttendanceTrend(days);
    }
}