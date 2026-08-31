import { useQuery } from '@tanstack/react-query'; import { getLoans, getExceptions } from '../services/loanService';
export const useLoans = () => useQuery({ queryKey:['loans'], queryFn:getLoans }); export const useExceptions = () => useQuery({ queryKey:['exceptions'], queryFn:getExceptions });
