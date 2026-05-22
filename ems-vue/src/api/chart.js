import request from './request'

export const basicStats = () => request.get('/chart/basicStats')
export const employeeDistribution = () => request.get('/chart/employeeDistribution')
export const attendanceTrend = (days = 30) => request.get('/chart/attendanceTrend', { params: { days } })
export const salaryStats = () => request.get('/chart/salaryStats')
export const leaveStats = () => request.get('/chart/leaveStats')
export const attendanceStats = () => request.get('/chart/attendanceStats')