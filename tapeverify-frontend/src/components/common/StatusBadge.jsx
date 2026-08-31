const styles={VALID:'bg-emerald-500/15 text-emerald-300',EXCEPTION:'bg-rose-500/15 text-rose-300',RESOLVED:'bg-cyan-500/15 text-cyan-300',PENDING:'bg-amber-500/15 text-amber-300'};
export default function StatusBadge({status}) { return <span className={`rounded-full px-2.5 py-1 text-xs font-bold ${styles[status]||styles.PENDING}`}>{status}</span>; }
