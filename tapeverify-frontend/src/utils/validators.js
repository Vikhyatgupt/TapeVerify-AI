export const required=value=>value!=null&&String(value).trim()!=='';
export const isPositive=value=>Number(value)>0;
export const isValidRate=value=>Number(value)>0&&Number(value)<=30;
