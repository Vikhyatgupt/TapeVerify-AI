import { useState } from 'react';

const numberOrNull = value => value === '' ? null : Number(value);

export default function ManualOverrideForm({loan,onSubmit,saving,hasSuggestion}) {
  const [form,setForm]=useState({borrowerName:loan.borrowerName||'',principalAmount:loan.principalAmount??'',currentBalance:loan.currentBalance??'',interestRate:loan.interestRate??'',originationDate:loan.originationDate||'',maturityDate:loan.maturityDate||'',paymentStatus:loan.paymentStatus||'',daysPastDue:loan.daysPastDue??'',borrowerState:loan.borrowerState||'',documentStatus:loan.documentStatus||'',lastUpdatedAt:loan.lastUpdatedAt?.slice(0,16)||'',sourceSystem:loan.sourceSystem||'',applyAiSuggestion:false});
  const change=e=>setForm({...form,[e.target.name]:e.target.type==='checkbox'?e.target.checked:e.target.value});
  const fields=[['borrowerName','Borrower','text'],['principalAmount','Principal','number'],['currentBalance','Current balance','number'],['interestRate','Rate (%)','number'],['originationDate','Origination','date'],['maturityDate','Maturity','date'],['paymentStatus','Payment status','text'],['daysPastDue','Days past due','number'],['borrowerState','State','text'],['documentStatus','Document status','text'],['lastUpdatedAt','Last updated','datetime-local'],['sourceSystem','Source system','text']];
  const submit = event => {
    event.preventDefault();
    onSubmit({...form,principalAmount:numberOrNull(form.principalAmount),currentBalance:numberOrNull(form.currentBalance),interestRate:numberOrNull(form.interestRate),daysPastDue:numberOrNull(form.daysPastDue),lastUpdatedAt:form.lastUpdatedAt||null,borrowerState:form.borrowerState||null,sourceSystem:form.sourceSystem||null});
  };
  return <form onSubmit={submit} className="panel"><p className="label">Human review</p><h2 className="mt-1 font-semibold">Correct and resolve</h2><div className="mt-4 grid gap-3 sm:grid-cols-2">{fields.map(([name,label,type])=><label key={name} className="text-xs text-slate-400">{label}<input className="input" name={name} type={type} value={form[name]} onChange={change}/></label>)}</div>{hasSuggestion&&<label className="mt-4 flex gap-2 text-xs text-cyan-200"><input type="checkbox" name="applyAiSuggestion" checked={form.applyAiSuggestion} onChange={change}/> Record that this human-reviewed correction used AI guidance</label>}<p className="mt-4 text-xs text-slate-500">AI guidance is advisory. This action is a human-approved correction.</p><button disabled={saving} className="mt-5 rounded-lg bg-emerald-400 px-4 py-2 text-sm font-bold text-emerald-950 disabled:opacity-50">{saving?'Saving…':'Resolve loan'}</button></form>;
}
