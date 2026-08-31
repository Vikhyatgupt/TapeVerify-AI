import { useQuery } from '@tanstack/react-query';
import api from '../services/apiClient';
import { useAuth } from '../context/AuthContext';
import BatchSummaryCard from '../components/dashboard/BatchSummaryCard';
import HealthChart from '../components/dashboard/HealthChart';

export default function DashboardPage() {
  const { user } = useAuth();
  const summary = useQuery({ queryKey: ['summary'], queryFn: () => api.get('/summary').then(r => r.data) });
  const data = summary.data || {};
  const label = user?.role === 'OPERATOR' ? 'Data operator' : user?.role === 'DATA_CONSUMER' ? 'Data consumer' : 'Reviewer';
  return <><p className="label">{label} dashboard</p><h1 className="mt-1 text-3xl font-bold">Loan tape health</h1><p className="mt-2 max-w-3xl text-sm text-slate-400">A role-aware view of data ingestion, verification progress, and records that still need human attention.</p>
    {summary.error ? <p className="mt-5 text-rose-300">Could not reach the API. Start the backend or update VITE_API_BASE_URL.</p> : <><div className="mt-7 grid gap-4 sm:grid-cols-2 xl:grid-cols-5"><BatchSummaryCard label="Total records" value={summary.isLoading ? '…' : data.totalLoans}/><BatchSummaryCard label="Import batches" value={summary.isLoading ? '…' : data.totalBatches}/><BatchSummaryCard label="Open exceptions" value={summary.isLoading ? '…' : data.openExceptions} tone="text-rose-300"/><BatchSummaryCard label="Verified records" value={summary.isLoading ? '…' : data.verifiedLoans} tone="text-emerald-300"/><BatchSummaryCard label="Quality score" value={summary.isLoading ? '…' : `${data.qualityScore || 0}%`} tone="text-cyan-300"/></div><div className="mt-6"><HealthChart valid={(data.totalLoans || 0) - (data.openExceptions || 0) - (data.resolvedLoans || 0)} exceptions={data.openExceptions || 0} resolved={data.resolvedLoans || 0}/></div></>}
    <section className="panel mt-6"><p className="label">Next step</p><p className="mt-2 text-slate-300">{user?.role === 'OPERATOR' ? 'Upload a primary tape, then upload the servicer and document sources to surface conflicts.' : user?.role === 'DATA_CONSUMER' ? 'Inspect verified records, export the approved dataset, and open the audit trail for source evidence.' : 'Prioritize high-severity exceptions, use the advisory copilot for context, then record your explicit human decision.'}</p></section></>;
}
