import { useMutation } from '@tanstack/react-query'; import { explainLoan } from '../services/aiService';
export const useAiCopilot=()=>useMutation({mutationFn:explainLoan});
