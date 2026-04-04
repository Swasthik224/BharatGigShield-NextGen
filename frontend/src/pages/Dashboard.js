import React from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  AreaChart, Area, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import api from '../api/client';
import StatCard from '../components/StatCard';
import RiskBadge from '../components/RiskBadge';

const BCR_DATA = [
  {week:'W1',bcr:0.42,target:0.62},{week:'W2',bcr:0.55,target:0.62},
  {week:'W3',bcr:0.61,target:0.62},{week:'W4',bcr:0.58,target:0.62},
  {week:'W5',bcr:0.67,target:0.62},{week:'W6',bcr:0.72,target:0.62},
  {week:'W7',bcr:0.65,target:0.62},{week:'W8',bcr:0.59,target:0.62},
];
const PIE_DATA = [
  {name:'Paid',value:1240,color:'#22d3ee'},
  {name:'Pending',value:85,color:'#f97316'},
  {name:'Rejected',value:42,color:'#ef4444'},
];
const ENV_DATA = [
  {date:'01 Feb',aqi:310,rain:12,heat:38},{date:'05 Feb',aqi:285,rain:67,heat:40},
  {date:'10 Feb',aqi:340,rain:8,heat:44},{date:'15 Feb',aqi:290,rain:55,heat:42},
  {date:'20 Feb',aqi:375,rain:90,heat:43},{date:'25 Feb',aqi:260,rain:20,heat:38},
];

const tt = { contentStyle:{background:'#1e293b',border:'1px solid #334155',borderRadius:8}, labelStyle:{color:'#94a3b8'} };

export default function Dashboard() {
  const { data: stats } = useQuery({
    queryKey: ['claimStats'],
    queryFn: () => api.get('/admin/claims/stats').then(r=>r.data.data),
  });
  const { data: triggers } = useQuery({
    queryKey: ['recentTriggers'],
    queryFn: () => api.get('/admin/triggers/recent?days=7').then(r=>r.data.data),
  });

  const h1 = { fontSize:22, fontWeight:700, marginBottom:20, color:'#f1f5f9' };
  const card = { background:'#1e293b', borderRadius:12, padding:20, border:'1px solid #334155' };
  const cardTitle = { color:'#64748b', fontSize:11, textTransform:'uppercase', letterSpacing:1, marginBottom:16 };

  return (
    <div>
      <h1 style={h1}>Overview</h1>
      {/* KPI Row */}
      <div style={{ display:'grid', gridTemplateColumns:'repeat(4,1fr)', gap:16, marginBottom:24 }}>
        <StatCard label="Active Workers"    value="18,310" sub="Across all pools" trend={8} />
        <StatCard label="Total Claims"      value={stats ? (stats.paid+stats.pending+stats.rejected) : '…'} sub="All time" trend={12} color="#f97316" />
        <StatCard label="Avg BCR"           value="0.62"   sub="Target: 0.55–0.70" color="#22c55e" />
        <StatCard label="Premium Collected" value="₹46.2L" sub="This year" trend={5} color="#a78bfa" />
      </div>

      {/* BCR Chart */}
      <div style={{...card, marginBottom:24}}>
        <div style={cardTitle}>BCR Trend (Weekly)</div>
        <ResponsiveContainer width="100%" height={220}>
          <AreaChart data={BCR_DATA}>
            <defs>
              <linearGradient id="bg" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%"  stopColor="#22d3ee" stopOpacity={0.25}/>
                <stop offset="95%" stopColor="#22d3ee" stopOpacity={0}/>
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="#334155"/>
            <XAxis dataKey="week" tick={{fill:'#64748b',fontSize:11}}/>
            <YAxis domain={[0.3,1.0]} tick={{fill:'#64748b',fontSize:11}}/>
            <Tooltip {...tt}/>
            <Area type="monotone" dataKey="bcr" stroke="#22d3ee" fill="url(#bg)" strokeWidth={2} name="BCR"/>
            <Area type="monotone" dataKey="target" stroke="#22c55e" fill="none" strokeDasharray="5 5" strokeWidth={1.5} name="Target"/>
          </AreaChart>
        </ResponsiveContainer>
      </div>

      {/* Row: Pie + Bar */}
      <div style={{ display:'grid', gridTemplateColumns:'1fr 1fr', gap:16, marginBottom:24 }}>
        <div style={card}>
          <div style={cardTitle}>Claim Status</div>
          <div style={{ display:'flex', alignItems:'center', gap:16 }}>
            <ResponsiveContainer width="50%" height={160}>
              <PieChart>
                <Pie data={PIE_DATA} cx="50%" cy="50%" innerRadius={40} outerRadius={65} dataKey="value" paddingAngle={3}>
                  {PIE_DATA.map((e,i)=><Cell key={i} fill={e.color}/>)}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
            <div>
              {PIE_DATA.map(p=>(
                <div key={p.name} style={{ display:'flex', alignItems:'center', gap:8, marginBottom:10 }}>
                  <div style={{ width:10, height:10, borderRadius:'50%', background:p.color }}/>
                  <span style={{ color:'#94a3b8', fontSize:13, width:56 }}>{p.name}</span>
                  <span style={{ color:'#f1f5f9', fontWeight:700 }}>{p.value}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
        <div style={card}>
          <div style={cardTitle}>Environmental Readings</div>
          <ResponsiveContainer width="100%" height={160}>
            <BarChart data={ENV_DATA} barGap={2}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155"/>
              <XAxis dataKey="date" tick={{fill:'#64748b',fontSize:10}}/>
              <YAxis tick={{fill:'#64748b',fontSize:10}}/>
              <Tooltip {...tt}/>
              <Bar dataKey="aqi"  fill="#f97316" name="AQI"      radius={[3,3,0,0]}/>
              <Bar dataKey="rain" fill="#3b82f6" name="Rain (mm)" radius={[3,3,0,0]}/>
              <Bar dataKey="heat" fill="#ef4444" name="Temp (°C)" radius={[3,3,0,0]}/>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Recent Triggers Table */}
      <div style={card}>
        <div style={cardTitle}>Recent Trigger Events (last 7 days)</div>
        {!triggers || triggers.length === 0 ? (
          <div style={{ color:'#475569', textAlign:'center', padding:'32px 0' }}>No triggers in last 7 days</div>
        ) : (
          <table style={{ width:'100%', borderCollapse:'collapse', fontSize:13 }}>
            <thead>
              <tr style={{ color:'#64748b', fontSize:11, textTransform:'uppercase', letterSpacing:1 }}>
                {['City','Type','Value','Date','Source'].map(h=>(
                  <th key={h} style={{ padding:'8px 12px', textAlign:'left', borderBottom:'1px solid #334155' }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {triggers.map(t=>(
                <tr key={t.id} style={{ borderBottom:'1px solid #33415550' }}>
                  <td style={{ padding:'10px 12px', fontWeight:600 }}>{t.city}</td>
                  <td style={{ padding:'10px 12px' }}><RiskBadge type={t.riskType}/></td>
                  <td style={{ padding:'10px 12px', color:'#fbbf24', fontFamily:'monospace' }}>{t.observedValue}</td>
                  <td style={{ padding:'10px 12px', color:'#64748b' }}>{t.eventDate}</td>
                  <td style={{ padding:'10px 12px', color:'#64748b' }}>{t.dataSource}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
