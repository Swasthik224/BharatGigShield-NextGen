import React, { useState } from 'react';
import { Outlet, NavLink, useNavigate } from 'react-router-dom';

const NAV = [
  { to: '/',          label: 'Overview',        icon: '◉' },
  { to: '/triggers',  label: 'Trigger Monitor', icon: '⚡' },
  { to: '/pools',     label: 'Risk Pools',      icon: '⬡' },
  { to: '/actuarial', label: 'Actuarial',       icon: '📊' },
];

export default function Layout() {
  const navigate = useNavigate();
  const logout = () => { localStorage.removeItem('jwt_token'); navigate('/login'); };

  return (
    <div style={{ display:'flex', minHeight:'100vh', background:'#0f172a', color:'#f1f5f9', fontFamily:'system-ui,sans-serif' }}>
      {/* Sidebar */}
      <aside style={{ width:220, background:'#1e293b', borderRight:'1px solid #334155', display:'flex', flexDirection:'column', padding:'0' }}>
        <div style={{ padding:'24px 20px 16px', borderBottom:'1px solid #334155' }}>
          <div style={{ display:'flex', alignItems:'center', gap:10 }}>
            <div style={{ width:32, height:32, background:'#22d3ee', borderRadius:8, display:'flex', alignItems:'center', justifyContent:'center', fontWeight:900, color:'#0f172a', fontSize:14 }}>G</div>
            <div>
              <div style={{ fontWeight:700, fontSize:15 }}>GigShield</div>
              <div style={{ fontSize:11, color:'#64748b' }}>Ops Dashboard</div>
            </div>
          </div>
        </div>
        <nav style={{ flex:1, padding:'12px 0' }}>
          {NAV.map(n => (
            <NavLink key={n.to} to={n.to} end={n.to==='/'} style={({ isActive }) => ({
              display:'flex', alignItems:'center', gap:10, padding:'10px 20px',
              color: isActive ? '#22d3ee' : '#94a3b8',
              background: isActive ? '#22d3ee18' : 'transparent',
              textDecoration:'none', fontSize:14, borderLeft: isActive ? '2px solid #22d3ee' : '2px solid transparent',
              transition:'all .15s'
            })}>
              <span style={{ fontSize:16 }}>{n.icon}</span> {n.label}
            </NavLink>
          ))}
        </nav>
        <div style={{ padding:'16px 20px', borderTop:'1px solid #334155' }}>
          <div style={{ display:'flex', alignItems:'center', gap:6, marginBottom:12 }}>
            <span style={{ width:8, height:8, borderRadius:'50%', background:'#22c55e', display:'inline-block' }}/>
            <span style={{ fontSize:12, color:'#22c55e' }}>Live</span>
          </div>
          <button onClick={logout} style={{ width:'100%', padding:'8px', background:'#334155', border:'none', borderRadius:8, color:'#94a3b8', cursor:'pointer', fontSize:13 }}>
            Logout
          </button>
        </div>
      </aside>
      {/* Main */}
      <main style={{ flex:1, overflow:'auto', padding:'28px 32px' }}>
        <Outlet />
      </main>
    </div>
  );
}
