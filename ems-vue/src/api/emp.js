import request from './request'

export const empPaged = data => request.post('/emp/paged', data)
export const empSave = data => request.post('/emp/save', data)
export const empDelete = id => request.delete(`/emp/${id}`)
export const empGetById = id => request.get(`/emp/${id}`)
export const empListAll = () => request.get('/emp/listAll')