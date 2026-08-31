import api from './apiClient'; export const explainLoan = (id, exceptionId) => api.post(`/loans/${id}/exceptions/${exceptionId}/explain`).then(r => r.data);
