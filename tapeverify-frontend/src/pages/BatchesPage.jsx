import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import api from '../services/apiClient';

export default function BatchesPage() {
  const [id, setId] = useState(null);
  const batches = useQuery({ queryKey: ['batches'], queryFn: () => api.get('/batches').then(r => r.data) });
  const detail = useQuery({ queryKey: ['batch', id], queryFn: () => api.get(`/batches/${id}`).then(r => r.data), enabled: Boolean(id) });
  return <><p className="label">Ingestion history</p><h1 className="mt-1 text-3xl font-bold">Loan tape batches</h1><p className="mt-2 text-sm text-slate-400">Select a batch to inspect its source lineage and imported records.</p>
    <div className="panel mt-7 overflow-x-auto p-0"><table className="w-full text-left text-sm"><thead className="bg-slate-900 text-xs uppercase text-slate-400"><tr><th className="p-4">File</th><th>Source</th><th>Uploader</th><th>Imported</th><th>Total</th><th>Valid</th><th>Exceptions</th><th>Failed rows</th></tr></thead><tbody>{(batches.data || []).map(b => <tr key={b.batchId} onClick={() => setId(b.batchId)} className="cursor-pointer border-t border-slate-800 hover:bg-slate-800/50"><td className="p-4 font-medium text-white">{b.filename}</td><td>{b.sourceType}</td><td>{b.uploadedBy}</td><td>{b.uploadedAt && new Date(b.uploadedAt).toLocaleString()}</td><td>{b.totalRecords}</td><td className="text-emerald-300">{b.validRecords}</td><td className="text-rose-300">{b.exceptionRecords}</td><td>{b.failedRecords}</td></tr>)}{!batches.isLoading && !batches.data?.length && <tr><td colSpan="8" className="p-8 text-center text-slate-500">No uploaded batches yet.</td></tr>}</tbody></table></div>
    {detail.data && <section className="panel mt-6"><div className="flex items-center justify-between"><div><p className="label">Batch detail</p><h2 className="mt-1 font-semibold">{detail.data.batch.filename}</h2></div><button onClick={() => setId(null)} className="text-sm text-cyan-300">Close</button></div><p className="mt-2 text-sm text-slate-400">Source file {detail.data.batch.sourceType} · {detail.data.loans.length} normalized rows retained with lineage.</p><div className="mt-4 max-h-72 overflow-auto text-sm">{detail.data.loans.map(loan => <p key={loan.id} className="border-t border-slate-800 py-2">Row {loan.sourceRow}: {loan.sourceLoanId || 'Missing loan ID'} · {loan.status}</p>)}</div></section>}
    {batches.isError && <p className="mt-3 text-rose-300">Could not load batch history.</p>}</>;
}
