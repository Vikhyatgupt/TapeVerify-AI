import { money, date } from '../../utils/formatters';

const eventLabel = action => action.replaceAll('_', ' ').toLowerCase().replace(/\b\w/g, letter => letter.toUpperCase());
const dateTime = timestamp => new Intl.DateTimeFormat('en-IN', { dateStyle: 'medium', timeStyle: 'medium' }).format(new Date(timestamp));

function LoanDetails({title,loan}) {
  if (!loan) return null;
  return <section className="rounded-lg border border-slate-700 bg-slate-950/40 p-4">
    <h3 className="text-sm font-semibold text-cyan-200">{title}</h3>
    <dl className="mt-3 grid gap-x-6 gap-y-3 text-sm sm:grid-cols-2 lg:grid-cols-3">
      <div><dt className="label">Borrower</dt><dd className="mt-1 text-slate-100">{loan.borrowerName || 'Not provided'}</dd></div>
      <div><dt className="label">Batch ID</dt><dd className="mt-1 break-all text-slate-200">{loan.batchId}</dd></div>
      <div><dt className="label">Status</dt><dd className="mt-1 text-slate-200">{loan.status}</dd></div>
      <div><dt className="label">Principal amount</dt><dd className="mt-1 text-slate-200">{money(loan.principalAmount)}</dd></div>
      <div><dt className="label">Interest rate</dt><dd className="mt-1 text-slate-200">{loan.interestRate == null ? 'Not provided' : `${loan.interestRate}%`}</dd></div>
      <div><dt className="label">Origination date</dt><dd className="mt-1 text-slate-200">{date(loan.originationDate)}</dd></div>
      <div><dt className="label">Maturity date</dt><dd className="mt-1 text-slate-200">{date(loan.maturityDate)}</dd></div>
    </dl>
  </section>;
}

export default function AuditTimeline({entries}) {
  return <div className="panel">
    <p className="label">Immutable ledger</p>
    <h2 className="mt-1 font-semibold">Audit timeline</h2>
    <div className="mt-5 space-y-5">
      {entries?.map(item => <article key={item.id} className="border-l-2 border-cyan-500/40 pl-4">
        <div className="flex flex-wrap items-center justify-between gap-2">
          <p className="font-semibold text-white">{eventLabel(item.action)}</p>
          <span className="rounded-full bg-slate-800 px-2.5 py-1 text-xs text-slate-300">Record #{item.id}</span>
        </div>
        <dl className="mt-3 grid gap-2 text-sm sm:grid-cols-2">
          <div><dt className="label">Performed by</dt><dd className="mt-1 text-slate-200">{item.modifiedBy}</dd></div>
          <div><dt className="label">Recorded at</dt><dd className="mt-1 text-slate-200">{dateTime(item.timestamp)}</dd></div>
        </dl>
        <div className="mt-4 space-y-3">
          <LoanDetails title={item.oldDetails ? 'Loan details before this change' : 'Loan details at this event'} loan={item.oldDetails || item.newDetails}/>
          {item.oldDetails && <LoanDetails title="Loan details after this change" loan={item.newDetails}/>} 
          {!item.newDetails && <p className="text-sm text-slate-500">Readable loan details were not captured for this older audit record.</p>}
        </div>
      </article>)}
      {!entries?.length && <p className="text-sm text-slate-500">Select a loan to view its audit history.</p>}
    </div>
  </div>
}
