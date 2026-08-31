import api from './apiClient';
export const getLoans = () => api.get('/loans').then(r => r.data);
export const getExceptions = () => api.get('/loans/exceptions').then(r => r.data);
export const uploadTape = (file,sourceType='PRIMARY') => {const data=new FormData();data.append('file',file);return api.post('/loans/ingest',data,{params:{sourceType}}).then(r=>r.data);};
export const resolveLoan = (id, changes) => api.patch('/loans/'+id+'/resolve', changes).then(r => r.data);
export const reviewLoan = (id,data) => api.post('/loans/'+id+'/review',data).then(r=>r.data);
export const getReviewActions = id => api.get('/loans/'+id+'/review-actions').then(r=>r.data);
