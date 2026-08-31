import { useMutation, useQueryClient } from '@tanstack/react-query'; import { uploadTape } from '../services/loanService';
export const useFileUpload=()=>{const client=useQueryClient();return useMutation({mutationFn:({file,sourceType})=>uploadTape(file,sourceType),onSuccess:()=>client.invalidateQueries()});};
