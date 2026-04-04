import React from 'react';
export default function StatCard({ label, value, sub, color='#22d3ee', trend }) {
  return (
    <div style={{ background:'#1e293b', borderRadius:12, padding:'20px', border:'1px solid #334155' }}>
      <div style={{ color:'#64748b', fontSize:12, marginBottom:6, textTransform:'uppercase', letterSpacing:1 }}>{label}</div>
      <div style={{ color, fontSize:28, fontWeight:700, lineHeight:1 }}>{value}</div>
      {sub && <div style={{ color:'#475569', fontSize:12, marginTop:6 }}>{sub}</div>}
      {trend !== undefined && (
        <div style={{ color: trend>=0?'#22c55e':'#ef4444', fontSize:12, marginTop:8, fontWeight:600 }}>
          {trend>=0?'▲':'▼'} {Math.abs(trend)}% vs last week
        </div>
      )}
    </div>
  );
}
