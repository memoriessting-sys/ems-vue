import request from './request'

export const salaryPaged = data => request.post('/salary/paged', data)
export const salarySave = data => request.post('/salary/save', data)
export const salaryDelete = id => request.delete(`/salary/${id}`)
export const salaryGetById = id => request.get(`/salary/${id}`)
export const salaryStats = () => request.get('/salary/statistics')
export const salaryBatchPay = ids => request.post('/salary/batchPay', ids)