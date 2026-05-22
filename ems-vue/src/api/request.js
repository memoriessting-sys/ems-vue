import axios from 'axios'

const request = axios.create({
  baseURL: '/ems/api',
  timeout: 10000,
  withCredentials: true
})

request.interceptors.response.use(
  res => res.data,
  err => {
    if (err.response?.status === 302) {
      window.location.href = '/login'
    }
    return Promise.reject(err.response?.data?.msg || err.message)
  }
)

export default request