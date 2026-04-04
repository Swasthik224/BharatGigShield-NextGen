import axios from 'axios';

const api = axios.create({ baseURL: '/api/v1' });

api.interceptors.request.use(cfg => {
  const token = localStorage.getItem('jwt_token');
  if (token) cfg.headers.Authorization = `Bearer ${token}`;
  return cfg;
});

api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('jwt_token');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

export default api;
