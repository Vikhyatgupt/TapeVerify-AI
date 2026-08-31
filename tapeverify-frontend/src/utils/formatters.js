export const money = value => value == null ? '—' : new Intl.NumberFormat('en-US',{style:'currency',currency:'USD'}).format(value);
export const date = value => value ? new Intl.DateTimeFormat('en-US',{dateStyle:'medium'}).format(new Date(`${value}T00:00:00`)) : '—';
