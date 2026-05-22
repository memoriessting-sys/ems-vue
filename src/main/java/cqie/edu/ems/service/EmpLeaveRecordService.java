package cqie.edu.ems.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cqie.edu.ems.comm.PageQo;
import cqie.edu.ems.domain.entity.EmpLeaveRecord;
import cqie.edu.ems.domain.qo.EmpLeaveRecordQo;
import cqie.edu.ems.domain.vo.EmpLeaveRecordVo;
import cqie.edu.ems.mapper.EmpLeaveRecordMapper;
import cqie.edu.ems.mapper.SysDictMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class EmpLeaveRecordService {

    private static final Logger log = LoggerFactory.getLogger(EmpLeaveRecordService.class);

    @Autowired
    private EmpLeaveRecordMapper empLeaveRecordMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDictMapper sysDictMapper;

    // 获取剩余假期统计
    public Map<String, Object> getRemainingLeave() {
        Map<String, Object> remaining = new HashMap<>();

        // 获取当前用户ID（这里需要根据实际情况获取）
        Long userId = getCurrentUserId();

        // 获取当前年份
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // 设置查询条件
        EmpLeaveRecordQo qo = new EmpLeaveRecordQo();
        qo.setUser_id(userId);

        // 查询所有请假记录
        List<EmpLeaveRecord> records = empLeaveRecordMapper.list(qo);

        // 计算各类假期使用情况
        double annualLeaveUsed = 0;
        double sickLeaveUsed = 0;
        double compLeaveUsed = 0;
        double monthLeaveDays = 0;

        // 获取当前月份的开始和结束时间
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date monthStart = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date monthEnd = calendar.getTime();

        for (EmpLeaveRecord record : records) {
            if (record.getStatus() == 2) { // 审批通过的假期
                String leaveTypeCode = record.getLeaveTypeCode();
                Date leaveStartTime = record.getLeaveStartTime();
                Date leaveEndTime = record.getLeaveEndTime();

                if (leaveStartTime != null && leaveEndTime != null) {
                    long diff = leaveEndTime.getTime() - leaveStartTime.getTime();
                    long days = diff / (1000 * 60 * 60 * 24) + 1;

                    if (leaveTypeCode != null) {
                        switch (leaveTypeCode) {
                            case "年假":
                                annualLeaveUsed += days;
                                break;
                            case "病假":
                                sickLeaveUsed += days;
                                break;
                            case "事假":
                                compLeaveUsed += days;
                                break;
                        }
                    }

                    // 计算本月请假天数
                    if (leaveStartTime.before(monthEnd) && leaveEndTime.after(monthStart)) {
                        Date actualStart = leaveStartTime.before(monthStart) ? monthStart : leaveStartTime;
                        Date actualEnd = leaveEndTime.after(monthEnd) ? monthEnd : leaveEndTime;
                        long monthDiff = actualEnd.getTime() - actualStart.getTime();
                        monthLeaveDays += (double) monthDiff / (1000 * 60 * 60 * 24) + 1;
                    }
                }
            }
        }

        remaining.put("annualLeaveUsed", annualLeaveUsed);
        remaining.put("sickLeaveUsed", sickLeaveUsed);
        remaining.put("compLeaveUsed", compLeaveUsed);
        remaining.put("monthLeaveDays", monthLeaveDays);
        // 假设年假总天数为10天，事假为5天，病假无限制
        remaining.put("annualLeaveTotal", 10);
        remaining.put("compLeaveTotal", 5);
        return remaining;
    }

    // 辅助方法：获取当前用户ID (需要根据实际认证框架实现)
    private Long getCurrentUserId() {
        // 示例：从session中获取，实际应用中应从Spring Security等认证框架获取
        // HttpSession session = ((ServletRequestAttributes)
        // RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        // SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        // return loginUser != null ? loginUser.getId() : null;
        return 1L; // 暂时返回一个固定值用于测试
    }

    // 获取请假统计信息
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 获取当前月份的开始和结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date monthStart = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date monthEnd = calendar.getTime();

        // 修改统计逻辑：只要请假时间和本月有重叠就算
        EmpLeaveRecordQo qo = new EmpLeaveRecordQo();
        qo.setLeaveStartTimeEnd(monthEnd); // 请假开始时间早于本月末
        qo.setLeaveEndTimeStart(monthStart); // 请假结束时间晚于本月初

        // 查询本月所有请假记录
        List<EmpLeaveRecord> monthRecords = empLeaveRecordMapper.list(qo);
        statistics.put("monthLeaveCount", monthRecords.size());

        // 统计各状态数量
        long approvedCount = monthRecords.stream().filter(r -> r.getStatus() == 2).count();
        long pendingCount = monthRecords.stream().filter(r -> r.getStatus() == 1).count();
        long rejectedCount = monthRecords.stream().filter(r -> r.getStatus() == 3).count();

        statistics.put("approvedCount", approvedCount);
        statistics.put("pendingCount", pendingCount);
        statistics.put("rejectedCount", rejectedCount);

        return statistics;
    }

    /**
     * 分页查询请假记录
     *
     * @param pageQo 分页查询条件
     * @return 分页结果
     */
    public Map<String, Object> paged(PageQo<EmpLeaveRecordQo> pageQo) {
        try {
            PageHelper.startPage(pageQo.getPageIndex(), pageQo.getPageSize());
            List<EmpLeaveRecordVo> list = empLeaveRecordMapper.selectList(pageQo.getFilters());
            PageInfo<EmpLeaveRecordVo> pageInfo = new PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("total", pageInfo.getTotal());
            result.put("pages", pageInfo.getPages());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return result;
        } catch (Exception e) {
            log.error("分页查询请假记录失败", e);
            throw new RuntimeException("分页查询失败：" + e.getMessage());
        }
    }

    // 新增实体
    public int insert(EmpLeaveRecord empLeaveRecord) {
        return empLeaveRecordMapper.insert(empLeaveRecord);
    }

    // 修改实体
    public int update(EmpLeaveRecord empLeaveRecord) {
        return empLeaveRecordMapper.update(empLeaveRecord);
    }

    // 根据主键删除实体
    public int deleteById(Long id) {
        return empLeaveRecordMapper.deleteById(id);
    }

    // 根据主键获取实体
    public EmpLeaveRecord getById(Long id) {
        return empLeaveRecordMapper.getById(id);
    }

    // 请假审批
    public void approveLeave(Long leaveId, Integer status, String approveContent) {
        EmpLeaveRecord record = empLeaveRecordMapper.getById(leaveId);
        if (record == null) {
            throw new RuntimeException("请假记录不存在");
        }

        // 检查当前状态是否为待审核
        if (record.getStatus() != 1) {
            throw new RuntimeException("该请假记录不在待审核状态");
        }

        // 更新请假记录
        record.setStatus(status);
        record.setApproveContent(approveContent);
        record.setApproveTime(new Date());
        record.setApprove_user_id(getCurrentUserId());

        empLeaveRecordMapper.update(record);
    }

    // 销假处理
    public void handleReturn(Long leaveId) {
        EmpLeaveRecord record = empLeaveRecordMapper.getById(leaveId);
        if (record == null) {
            throw new RuntimeException("请假记录不存在");
        }

        // 检查当前状态是否为审批通过
        if (record.getStatus() != 2) {
            throw new RuntimeException("该请假记录不在审批通过状态");
        }

        // 更新请假记录
        record.setStatus(9); // 已销假
        record.setRealEndTime(new Date());

        empLeaveRecordMapper.update(record);
    }

    // 获取待审核的请假申请总数
    public int countPendingLeaves() {
        return empLeaveRecordMapper.countPendingLeaves();
    }

    /**
     * 统计请假记录数量
     *
     * @param qo 查询条件
     * @return 记录数量
     */
    public long count(EmpLeaveRecordQo qo) {
        return empLeaveRecordMapper.count(qo);
    }
}