import { createContext,useContext,useState } from 'react';
const LoanContext=createContext(null); export function LoanProvider({children}){const [selectedLoan,setSelectedLoan]=useState(null);return <LoanContext.Provider value={{selectedLoan,setSelectedLoan}}>{children}</LoanContext.Provider>} export const useLoanContext=()=>useContext(LoanContext);
