import api from './apiClient'; export const getAudit = id => api.get(`/loans/${id}/audit`).then(r => r.data); export const getIntegrity = id => api.get(`/loans/${id}/integrity`).then(r=>r.data);
