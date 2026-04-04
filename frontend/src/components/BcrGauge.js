import React from 'react';
export default function BcrGauge({ bcr }) {
  const pct = Math.min((bcr/1.0)*100, 100);
  const color = bcr<0.55?'#22d3ee':bcr<=0.70?'#22c55e':bcr<=0.85?'#f97316':'#ef4444';
  const status = bcr<0.55?'Under-priced':bcr<=0.70?'Healthy':bcr<=0.85?'Watch':'SUSPEND';
  return (
    <div>
      <div style={{ display:'flex', justifyContent:'space-between', fontSize:11, color:'#475569', marginBottom:4 }}>
        <span>0</span><span>0.55</span><span>0.70</span><span>0.85</span><span>1.0</span>
      </div>
      <div style={{ height:12, background:'#334155', borderRadius:6, overflow:'hidden', position:'relative' }}>
        <div style={{ position:'absolute', height:'100%', width:pct+'%', background:color, borderRadius:6, transition:'width .5s' }}/>
        <div style={{ position:'absolute', left:'55%', width:'15%', height:'100%', border:'1px solid #22c55e55', borderLeft:'2px solid #22c55e', borderRight:'2px solid #22c55e' }}/>
      </div>
      <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center', marginTop:6 }}>
        <span style={{ color, fontWeight:700, fontSize:14 }}>BCR: {bcr.toFixed(3)}</span>
        <span style={{ background:color+'22', color, fontSize:11, padding:'2px 8px', borderRadius:12, fontWeight:700 }}>{status}</span>
      </div>
    </div>
  );
}
