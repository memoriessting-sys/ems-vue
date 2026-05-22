import request from './request'

export const deptPaged = data => request.post('/dept/paged', data)
export const deptSave = data => request.post('/dept/save', data)
export const deptDelete = id => request.delete(`/dept/${id}`)
export const deptTree = excludeRootId => request.post('/dept/tree', null, { params: { excludeRootId } })
export const deptListAll = () => request.get('/dept/listAll')