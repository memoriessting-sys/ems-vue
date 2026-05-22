import request from './request'

export const leavePaged = data => request.post('/leave/paged', data)
export const leaveSave = data => request.post('/leave/save', data)
export const leaveDelete = id => request.delete(`/leave/${id}`)
export const leaveGetById = id => request.get(`/leave/${id}`)
export const leaveApprove = (leaveId, status, content) => request.post('/leave/approve', null, { params: { leaveId, status, approveContent: content } })
export const leaveReturn = id => request.post(`/leave/return/${id}`)
export const leaveStats = () => request.get('/leave/statistics')