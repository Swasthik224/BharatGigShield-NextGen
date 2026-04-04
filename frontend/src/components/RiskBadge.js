import React from 'react';
const CFG = { AQI:['#f9731620','#f97316','🌫'], RAIN:['#3b82f620','#3b82f6','🌧'], HEAT:['#ef444420','#ef4444','🔥'] };
export default function RiskBadge({ type }) {
  const [bg,color,icon] = CFG[type]||['#33415520','#94a3b8',''];
  return <span style={{ background:bg, color, fontSize:11, fontWeight:700, padding:'2px 8px', borderRadius:6 }}>{icon} {type}</span>;
}
