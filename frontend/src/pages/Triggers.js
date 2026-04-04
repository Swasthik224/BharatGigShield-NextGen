import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ReferenceLine, ResponsiveContainer } from 'recharts';
import api from '../api/client';
import RiskBadge from '../components/RiskBadge';

const CITIES    = ['Delhi','Mumbai','Chennai','Kolkata'];
const TYPES     = ['AQI','RAIN','HEAT'];
const ENV_DATA  = [
  {date:'01 Feb',aqi:310,rain:12,heat:38},{date:'05 Feb',aqi:285,rain:67,heat:40},
  {date:'10 Feb',aqi:340,rain:8, heat:44},{date:'15 Feb',aqi:290,rain:55,heat:42},
  {date:'20 Feb',aqi:375,rain:90,heat:43},{date:'25 Feb',aqi:260,rain:20,heat:38},
];

export default function Triggers() {
  const [city, setCity]         = useState('Delhi');
  const [type, setType]         = useState('AQI');
  const [value, setValue]       = useState('');
  const [simMsg, setSimMsg]     = useState('');
  const qc = useQueryClient();

  const { data: triggers, isLoading } = useQuery({
    queryKey: ['triggers30'],
    queryFn: () => api.get('/admin/triggers/recent?days=30').then(r=>r.data.data),
  });

  const simulate = useMutation({
    mutationFn: () => api.post('/admin/triggers/simulate', { city, riskType: type, observedValue: parseFloat(value) }),
    onSuccess: (res) => {
      setSimMsg(`✅ Trigger fired for ${city} ${type} = ${value}. Claims processing started.`);
      qc.invalidateQueries({ queryKey: ['triggers30'] });
    },
    onError: (e) => setSimMsg('❌ Error: ' + (e.response?.data?.message || e.message)),
  });

  const card = { background:'#1e293b', borderRadius:12, padding:20, border:'1px solid #334155', marginBottom:20 };
  const input = { background:'#0f172a', border:'1px solid #334155', borderRadius:8, padding:'8px 12px', color:'#f1f5f9', fontSize:14, outline:'none' };
  const btn = { background:'#22d3ee', border:'none', borderRadius:8, padding:'8px 18px', color:'#0f172a', fontWeight:700, cursor:'pointer', fontSize:14 };
  const tt = { contentStyle:{background:'#1e293b',border:'1px solid #334155',borderRadius:8}, labelStyle:{color:'#94a3b8'} };

  return (
    <div>
      <h1 style={{ fontSize:22, fontWeight:700, marginBottom:20, color:'#f1f5f9' }}>Trigger Monitor</h1>

      {/* Chart */}
      <div style={card}>
        <div style={{ color:'#64748b', fontSize:11, textTransform:'uppercase', letterSpacing:1, marginBottom:16 }}>Observed Values vs Thresholds (Last 30 days)</div>
        <ResponsiveContainer width="100%" height={260}>
          <LineChart data={ENV_DATA}>
            <CartesianGrid strokeDasharray="3 3" stroke="#334155"/>
            <XAxis dataKey="date" tick={{fill:'#64748b',fontSize:11}}/>
            <YAxis tick={{fill:'#64748b',fontSize:11}}/>
            <Tooltip {...tt}/>
            <Legend wrapperStyle={{fontSize:12,color:'#94a3b8'}}/>
            <Line type="monotone" dataKey="aqi"  stroke="#f97316" strokeWidth={2} dot={{r:3}} name="AQI"/>
            <Line type="monotone" dataKey="rain" stroke="#3b82f6" strokeWidth={2} dot={{r:3}} name="Rain mm"/>
            <Line type="monotone" dataKey="heat" stroke="#ef4444" strokeWidth={2} dot={{r:3}} name="Temp °C"/>
            <ReferenceLine y={300} stroke="#f9731680" strokeDasharray="5 5" label={{value:'AQI 300',fill:'#f97316',fontSize:10}}/>
            <ReferenceLine y={50}  stroke="#3b82f680" strokeDasharray="5 5" label={{value:'Rain 50mm',fill:'#3b82f6',fontSize:10}}/>
            <ReferenceLine y={42}  stroke="#ef444480" strokeDasharray="5 5" label={{value:'Temp 42°C',fill:'#ef4444',fontSize:10}}/>
          </LineChart>
        </ResponsiveContainer>
      </div>

      {/* Simulate trigger */}
      <div style={card}>
        <div style={{ color:'#64748b', fontSize:11, textTransform:'uppercase', letterSpacing:1, marginBottom:16 }}>🔧 Simulate Trigger (Dev/Test)</div>
        <div style={{ display:'flex', gap:12, alignItems:'center', flexWrap:'wrap' }}>
          <select value={city} onChange={e=>setCity(e.target.value)} style={input}>
            {CITIES.map(c=><option key={c}>{c}</option>)}
          </select>
          <select value={type} onChange={e=>setType(e.target.value)} style={input}>
            {TYPES.map(t=><option key={t}>{t}</option>)}
          </select>
          <input type="number" placeholder={type==='AQI'?'e.g. 320':type==='RAIN'?'e.g. 65':'e.g. 44'}
            value={value} onChange={e=>setValue(e.target.value)} style={{...input,width:120}}/>
          <button style={btn} onClick={()=>simulate.mutate()} disabled={simulate.isPending||!value}>
            {simulate.isPending ? 'Firing…' : '⚡ Fire Trigger'}
          </button>
        </div>
        {simMsg && <div style={{ marginTop:12, padding:'10px 14px', borderRadius:8, background: simMsg.startsWith('✅')?'#064e3b':'#450a0a', color: simMsg.startsWith('✅')?'#6ee7b7':'#fca5a5', fontSize:13 }}>{simMsg}</div>}
      </div>

      {/* Trigger history table */}
      <div style={card}>
        <div style={{ color:'#64748b', fontSize:11, textTransform:'uppercase', letterSpacing:1, marginBottom:16 }}>Recent Trigger Events</div>
        {isLoading ? <div style={{color:'#475569',textAlign:'center',padding:32}}>Loading…</div> :
        !triggers || triggers.length === 0 ? <div style={{color:'#475569',textAlign:'center',padding:32}}>No triggers found</div> : (
          <table style={{ width:'100%', borderCollapse:'collapse', fontSize:13 }}>
            <thead>
              <tr style={{ color:'#64748b', fontSize:11, textTransform:'uppercase' }}>
                {['City','Type','Observed','Threshold','Date','Source','Validated'].map(h=>(
                  <th key={h} style={{ padding:'8px 12px', textAlign:'left', borderBottom:'1px solid #334155' }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {triggers.map(t=>(
                <tr key={t.id} style={{ borderBottom:'1px solid #33415550' }}>
                  <td style={{ padding:'10px 12px', fontWeight:600 }}>{t.city}</td>
                  <td style={{ padding:'10px 12px' }}><RiskBadge type={t.riskType}/></td>
                  <td style={{ padding:'10px 12px', color:'#fbbf24', fontFamily:'monospace', fontWeight:700 }}>{t.observedValue}</td>
                  <td style={{ padding:'10px 12px', color:'#64748b', fontFamily:'monospace' }}>{t.thresholdValue}</td>
                  <td style={{ padding:'10px 12px', color:'#94a3b8' }}>{t.eventDate}</td>
                  <td style={{ padding:'10px 12px', color:'#64748b' }}>{t.dataSource}</td>
                  <td style={{ padding:'10px 12px' }}>
                    <span style={{ color: t.validated ? '#22c55e' : '#f97316', fontSize:12 }}>
                      {t.validated ? '✓' : '○'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
