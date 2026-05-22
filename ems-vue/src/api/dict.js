import request from './request'

export const dictPaged = data => request.post('/dict/paged', data)
export const dictSave = data => request.post('/dict/save', data)
export const dictDelete = id => request.delete(`/dict/${id}`)