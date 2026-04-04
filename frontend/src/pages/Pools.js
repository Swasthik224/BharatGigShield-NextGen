import React from 'react';
import BcrGauge from '../components/BcrGauge';
import RiskBadge from '../components/RiskBadge';

const POOLS = [
  { city:'Delhi',   riskType:'AQI',  enrolled:4820, bcr:0.61, premiumL:9.64,  claimsL:5.88,  suspended:false },
  { city:'Delhi',   riskType:'HEAT', enrolled:2100, bcr:0.48, premiumL:4.20,  claimsL:2.02,  suspended:false },
  { city:'Mumbai',  riskType:'RAIN', enrolled:6340, bcr:0.73, premiumL:12.68, claimsL:9.26,  suspended:false },
  { city:'Chennai', riskType:'RAIN', enrolled:3200, bcr:0.87, premiumL:6.40,  claimsL:5.57,  suspended:true  },
  { city:'Chennai', riskType:'HEAT', enrolled:1100, bcr:0.52, premiumL:2.20,  claimsL:1.14,  suspended:false },
  { city:'Kolkata', riskType:'AQI',  enrolled:1850, bcr:0.55, premiumL:3.70,  claimsL:2.04,  suspended:false },
  { city:'Kolkata', riskType:'RAIN', enrolled:2400, bcr:0.66, premiumL:4.80,  claimsL:3.17,  suspended:false },
];

export default function Pools() {
  const card = { background:'#1e293b', borderRadius:12, padding:20, border:'1px solid #334155' };

  return (
    <div>
      <h1 style={{ fontSize:22, fontWeight:700, marginBottom:8, color:'#f1f5f9' }}>City Risk Pools</h1>
      <p style={{ color:'#64748b', fontSize:13, marginBottom:24 }}>BCR target: 0.55–0.70 · Enrollment suspended if loss ratio &gt; 85%</p>

      <div style={{ display:'grid', gridTemplateColumns:'repeat(auto-fill,minmax(340px,1fr))', gap:16 }}>
        {POOLS.map((p,i)=>(
          <div key={i} style={{...card, border: p.suspended ? '1px solid #ef444460' : '1px solid #334155'}}>
            {/* Header */}
            <div style={{ display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:16 }}>
              <div style={{ display:'flex', alignItems:'center', gap:10 }}>
                <span style={{ fontWeight:700, fontSize:18 }}>{p.city}</span>
                <RiskBadge type={p.riskType}/>
                {p.suspended && (
                  <span style={{ background:'#ef444420', color:'#ef4444', fontSize:11, fontWeight:700, padding:'2px 8px', borderRadius:6 }}>⛔ SUSPENDED</span>
                )}
              </div>
              <span style={{ color:'#64748b', fontSize:13 }}>{p.enrolled.toLocaleString()} workers</span>
            </div>
            {/* BCR Gauge */}
            <BcrGauge bcr={p.bcr}/>
            {/* Stats */}
            <div style={{ display:'grid', gridTemplateColumns:'repeat(3,1fr)', gap:12, marginTop:16 }}>
              {[
                ['Premium','₹'+p.premiumL+'L'],
                ['Claims Paid','₹'+p.claimsL+'L'],
                ['Loss Ratio', (p.bcr*100).toFixed(1)+'%'],
              ].map(([label,val])=>(
                <div key={label} style={{ background:'#0f172a', borderRadius:8, padding:'10px 12px' }}>
                  <div style={{ color:'#475569', fontSize:11, marginBottom:4 }}>{label}</div>
                  <div style={{ color: label==='Loss Ratio'?(p.bcr>0.85?'#ef4444':'#22c55e'):'#f1f5f9', fontWeight:700, fontSize:15 }}>{val}</div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
