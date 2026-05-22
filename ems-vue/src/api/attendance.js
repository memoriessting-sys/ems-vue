import request from './request'

export const attendancePaged = data => request.post('/attendance/paged', data)
export const attendanceSave = data => request.post('/attendance/save', data)
export const attendanceDelete = id => request.delete(`/attendance/${id}`)
export const attendanceGetById = id => request.get(`/attendance/${id}`)
export const checkIn = () => request.post('/attendance/checkIn')
export const checkOut = () => request.post('/attendance/checkOut')
export const attendanceStats = () => request.get('/attendance/statistics')
export const attendanceDepartments = () => request.get('/attendance/departments')
export const attendanceEmployees = () => request.get('/attendance/employees')