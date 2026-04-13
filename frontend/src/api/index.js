import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 10000
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  res => res.data,
  err => Promise.reject(err)
)

export const authApi = {
  login: (data) => api.post('/api/auth/login', data)
}

export const patientApi = {
  list: (name) => api.get('/api/patients', { params: { name } }),
  get: (id) => api.get(`/api/patients/${id}`),
  create: (data) => api.post('/api/patients', data),
  update: (id, data) => api.put(`/api/patients/${id}`, data),
  delete: (id) => api.delete(`/api/patients/${id}`)
}

export const visitApi = {
  list: () => api.get('/api/visits'),
  byPatient: (patientId) => api.get(`/api/visits/patient/${patientId}`),
  create: (data) => api.post('/api/visits', data),
  updateStatus: (id, status) => api.put(`/api/visits/${id}/status`, { status })
}

export const drugApi = {
  list: (name) => api.get('/api/drugs', { params: { name } }),
  create: (data) => api.post('/api/drugs', data),
  update: (id, data) => api.put(`/api/drugs/${id}`, data)
}

export const prescriptionApi = {
  byVisit: (visitId) => api.get(`/api/prescriptions/visit/${visitId}`),
  create: (data) => api.post('/api/prescriptions', data)
}
