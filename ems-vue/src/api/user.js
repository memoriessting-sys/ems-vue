import request from './request'

export const login = data => request.post('/user/login', data)
export const logout = () => request.post('/user/logout')
export const getUserInfo = () => request.get('/user/info')
export const register = data => request.post('/user/register', data)
export const forgotPassword = data => request.post('/user/forgotPassword', data)
export const userPaged = data => request.post('/user/paged', data)
export const userSave = data => request.post('/user/save', data)
export const userDelete = id => request.delete(`/user/${id}`)
export const resetPwd = id => request.put(`/user/resetPwd/${id}`)