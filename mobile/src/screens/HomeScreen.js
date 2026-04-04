import React from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, RefreshControl } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import api from '../api/client';

const RISK_COLORS = { AQI:'#f97316', RAIN:'#3b82f6', HEAT:'#ef4444' };

export default function HomeScreen({ navigation }) {
  const { data: policies, refetch, isRefetching } = useQuery({
    queryKey: ['my-policies'],
    queryFn: () => api.get('/policies/my').then(r=>r.data.data),
    retry: false,
  });
  const { data: claims } = useQuery({
    queryKey: ['my-claims'],
    queryFn: () => api.get('/claims/my').then(r=>r.data.data),
    retry: false,
  });
  const recentPayout = claims?.find(c=>c.status==='PAID');

  return (
    <ScrollView style={h.bg} refreshControl={
      <RefreshControl refreshing={isRefetching} onRefresh={refetch} tintColor="#22d3ee"/>}>

      <View style={h.header}>
        <Text style={h.brand}>GigShield</Text>
        <Text style={h.sub}>Your coverage dashboard</Text>
      </View>

      {recentPayout && (
        <View style={h.payoutBanner}>
          <Text style={h.payoutIcon}>💸</Text>
          <View>
            <Text style={h.payoutTitle}>Payout Received!</Text>
            <Text style={h.payoutAmt}>₹{recentPayout.payoutAmountRupees} · {recentPayout.riskType} trigger</Text>
          </View>
        </View>
      )}

      <Text style={h.section}>ACTIVE POLICIES</Text>

      {(policies||[]).length === 0 ? (
        <View style={h.empty}>
          <Text style={h.emptyIcon}>🛡️</Text>
          <Text style={h.emptyTitle}>No active policy</Text>
          <Text style={h.emptySub}>Protect your income from AQI, rain & heat events for as low as ₹20/week</Text>
        </View>
      ) : (
        (policies||[]).map(p=>(
          <View key={p.id} style={h.card}>
            <View style={{ flexDirection:'row', justifyContent:'space-between', alignItems:'center' }}>
              <View style={[h.badge, {backgroundColor: RISK_COLORS[p.riskType]+'22'}]}>
                <Text style={[h.badgeTxt, {color: RISK_COLORS[p.riskType]}]}>{p.riskType}</Text>
              </View>
              <Text style={h.active}>● ACTIVE</Text>
            </View>
            <Text style={h.city}>{p.city}</Text>
            <Text style={h.pnum}>{p.policyNumber}</Text>
            <View style={h.statsRow}>
              <View style={h.stat}><Text style={h.statLabel}>Weekly premium</Text><Text style={h.statVal}>₹{p.weeklyPremiumRupees}</Text></View>
              <View style={h.stat}><Text style={h.statLabel}>Payout/day</Text><Text style={h.statVal}>₹{p.payoutPerDayRupees}</Text></View>
              <View style={h.stat}><Text style={h.statLabel}>Next billing</Text><Text style={h.statVal}>{p.nextBillingDate}</Text></View>
            </View>
          </View>
        ))
      )}

      <TouchableOpacity style={h.cta} onPress={()=>navigation.navigate('BuyPolicy')}>
        <Text style={h.ctaTxt}>+ Add Policy · from ₹20/week</Text>
      </TouchableOpacity>

      <TouchableOpacity style={h.linkBtn} onPress={()=>navigation.navigate('Claims')}>
        <Text style={h.linkTxt}>View claims history →</Text>
      </TouchableOpacity>

      <View style={{ height:32 }}/>
    </ScrollView>
  );
}

const h = StyleSheet.create({
  bg:          { flex:1, backgroundColor:'#0f172a' },
  header:      { padding:24, paddingTop:52 },
  brand:       { fontSize:26, fontWeight:'800', color:'#22d3ee' },
  sub:         { color:'#475569', marginTop:2, fontSize:13 },
  payoutBanner:{ margin:16, backgroundColor:'#064e3b', borderRadius:14, padding:16, flexDirection:'row', alignItems:'center', gap:12 },
  payoutIcon:  { fontSize:32 },
  payoutTitle: { color:'#6ee7b7', fontWeight:'700', fontSize:16 },
  payoutAmt:   { color:'#a7f3d0', marginTop:2, fontSize:13 },
  section:     { color:'#475569', fontSize:11, letterSpacing:1, marginHorizontal:16, marginBottom:10 },
  card:        { backgroundColor:'#1e293b', margin:16, marginTop:0, borderRadius:16, padding:16, marginBottom:12 },
  badge:       { paddingHorizontal:10, paddingVertical:4, borderRadius:6 },
  badgeTxt:    { fontSize:12, fontWeight:'700' },
  active:      { color:'#22c55e', fontSize:11, fontWeight:'600' },
  city:        { color:'#f1f5f9', fontSize:22, fontWeight:'700', marginTop:10 },
  pnum:        { color:'#475569', fontSize:11, marginTop:2, marginBottom:12 },
  statsRow:    { flexDirection:'row', justifyContent:'space-between' },
  stat:        { flex:1 },
  statLabel:   { color:'#64748b', fontSize:11 },
  statVal:     { color:'#f1f5f9', fontSize:15, fontWeight:'600', marginTop:2 },
  empty:       { margin:16, padding:32, alignItems:'center', backgroundColor:'#1e293b', borderRadius:16 },
  emptyIcon:   { fontSize:48, marginBottom:12 },
  emptyTitle:  { color:'#94a3b8', fontSize:18, fontWeight:'600' },
  emptySub:    { color:'#475569', textAlign:'center', marginTop:8, lineHeight:20 },
  cta:         { margin:16, backgroundColor:'#22d3ee', borderRadius:12, padding:18, alignItems:'center' },
  ctaTxt:      { color:'#0f172a', fontWeight:'700', fontSize:16 },
  linkBtn:     { alignItems:'center', padding:12 },
  linkTxt:     { color:'#22d3ee', fontSize:14 },
});
