import request from './request'

export const postLevelPaged = data => request.post('/postLevel/paged', data)
export const postLevelSave = data => request.post('/postLevel/save', data)
export const postLevelDelete = id => request.delete(`/postLevel/${id}`)
export const postLevelGetById = id => request.get(`/postLevel/${id}`)
export const postLevelListAll = () => request.get('/postLevel/listAll')