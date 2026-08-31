import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import api from '../services/apiClient';
import { explainLoan } from '../services/aiService';
import { getReviewActions, resolveLoan, reviewLoan } from '../services/loanService';
import ExceptionTable from '../components/exceptions/ExceptionTable';
import AiExplanationPanel from '../components/copilot/AiExplanationPanel';
import ManualOverrideForm from '../components/copilot/ManualOverrideForm';

export default function ExceptionsPage() {
  const [selected, setSelected] = useState(null), [selectedException, setSelectedException] = useState(null);
  const [suggestion, setSuggestion] = useState(null), [query, setQuery] = useState(''), [severity, setSeverity] = useState(''), [page, setPage] = useState(0), [comment, setComment] = useState('');
  const client = useQueryClient();
  const queue = useQuery({ queryKey: ['exception-queue', page, query, severity], queryFn: () => api.get('/exceptions', { params: { page, size: 20, query: query || undefined, severity: severity || undefined } }).then(r => r.data) });
  const actions = useQuery({ queryKey: ['review-actions', selected?.id], queryFn: () => getReviewActions(selected.id), enabled: Boolean(selected) });
  const done = () => { client.invalidateQueries(); setSelected(null); setSelectedException(null); setSuggestion(null); setComment(''); };
  const explain = useMutation({ mutationFn: ({ id, exceptionId }) => explainLoan(id, exceptionId), onSuccess: setSuggestion });
  const resolve = useMutation({ mutationFn: ({ id, data }) => resolveLoan(id, data), onSuccess: done });
  const review = useMutation({ mutationFn: ({ action }) => reviewLoan(selected.id, { exceptionId: selectedException?.id, action, comment }), onSuccess: done });
  const loans = (queue.data?.items || []).map(item => ({ ...item.loan, exceptions: [item.exception] }));
  const choose = (loan, exception) => { setSelected(loan); setSelectedException(exception); setSuggestion(null); };
  return <>
    <p className="label">Exception queue</p><h1 className="mt-1 text-3xl font-bold">Review flagged records</h1>
    <div className="mt-5 flex flex-wrap gap-3"><input className="input max-w-sm" aria-label="Search exceptions" placeholder="Search loan ID, borrower ID, or borrower" value={query} onChange={e => { setQuery(e.target.value); setPage(0); }}/><select aria-label="Filter exception severity" className="input max-w-44" value={severity} onChange={e => { setSeverity(e.target.value); setPage(0); }}><option value="">All severities</option><option>HIGH</option><option>MEDIUM</option><option>ERROR</option></select></div>
    <div className="mt-5 grid gap-6 xl:grid-cols-[1.5fr_1fr]">{queue.isLoading ? <p className="text-slate-400">Loading exceptions…</p> : <div><ExceptionTable loans={loans} onSelect={choose}/><div className="mt-3 flex items-center justify-between text-sm text-slate-400"><span>{queue.data?.total || 0} open exceptions</span><div className="flex gap-2"><button disabled={page === 0} onClick={() => setPage(p => p - 1)} className="rounded border border-slate-700 px-3 py-1 disabled:opacity-40">Previous</button><button disabled={!queue.data || page + 1 >= queue.data.totalPages} onClick={() => setPage(p => p + 1)} className="rounded border border-slate-700 px-3 py-1 disabled:opacity-40">Next</button></div></div></div>}
      <div className="space-y-5">{selected ? <>
        <div className="panel text-sm"><p className="label">Selected exception · {selectedException?.severity}</p><p className="mt-2 text-rose-300">{selectedException?.errorMessage}</p><p className="mt-2 text-slate-400">Loan {selected.sourceLoanId || selected.id} · Row {selectedException?.rowNumber || '—'} · Raw: {selectedException?.rawValue || '—'}</p></div>
        <AiExplanationPanel suggestion={suggestion} loading={explain.isPending} onExplain={() => explain.mutate({ id: selected.id, exceptionId: selectedException.id })}/>
        <ManualOverrideForm key={selected.id} loan={selected} saving={resolve.isPending} hasSuggestion={Boolean(suggestion)} onSubmit={data => resolve.mutate({ id: selected.id, data })}/>
        <div className="panel"><p className="label">Reviewer decision</p><textarea aria-label="Reviewer comment" className="input mt-2" placeholder="Add a review comment or correction request" value={comment} onChange={e => setComment(e.target.value)}/><div className="mt-3 flex flex-wrap gap-2"><button onClick={() => review.mutate({ action: 'COMMENT' })} className="rounded bg-slate-700 px-3 py-2 text-sm">Save comment</button><button onClick={() => review.mutate({ action: 'REQUEST_CORRECTION' })} className="rounded bg-amber-400 px-3 py-2 text-sm font-semibold text-slate-950">Request correction</button><button onClick={() => review.mutate({ action: 'REJECT' })} className="rounded bg-rose-500 px-3 py-2 text-sm font-semibold text-white">Reject record</button><button onClick={() => review.mutate({ action: 'APPROVE' })} className="rounded bg-emerald-400 px-3 py-2 text-sm font-bold text-emerald-950">Approve verified record</button></div>{review.isError && <p className="mt-2 text-sm text-rose-300">{review.error.response?.data?.message || 'Review action failed.'}</p>}<div className="mt-4 text-xs text-slate-400">{(actions.data || []).map(a => <p key={a.id}>{a.action} · {a.actor} · {a.comment}</p>)}</div></div>
        {resolve.isError && <p className="text-sm text-rose-300">{resolve.error.response?.data?.message || 'Correction could not be saved.'}</p>}
      </> : <div className="panel text-sm text-slate-500">Select an exception to inspect, request AI guidance, and record a reviewer decision.</div>}</div></div>
    {queue.isError && <p className="mt-4 text-sm text-rose-300">Could not load the exception queue.</p>}
  </>;
}
