import React from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import api from '../api/client';

const STATUS_CFG = {
  PAID:         { color:'#22c55e',  icon:'✓' },
  APPROVED:     { color:'#22d3ee', icon:'◎' },
  FRAUD_CHECK:  { color:'#f97316', icon:'?' },
  PENDING:      { color:'#64748b', icon:'○' },
  REJECTED:     { color:'#ef4444', icon:'✕' },
};

export default function ClaimsScreen({ navigation }) {
  const { data: claims, isLoading } = useQuery({
    queryKey: ['my-claims'],
    queryFn: () => api.get('/claims/my').then(r=>r.data.data),
    retry: false,
  });

  const renderItem = ({ item: c }) => {
    const cfg = STATUS_CFG[c.status] || STATUS_CFG.PENDING;
    return (
      <View style={cl.card}>
        <View style={{ flexDirection:'row', justifyContent:'space-between', alignItems:'center' }}>
          <Text style={cl.claimNum}>{c.claimNumber}</Text>
          <View style={[cl.statusPill, {backgroundColor: cfg.color+'22'}]}>
            <Text style={[cl.statusTxt, {color: cfg.color}]}>{cfg.icon} {c.status}</Text>
          </View>
        </View>
        <View style={{ flexDirection:'row', justifyContent:'space-between', marginTop:10 }}>
          <View><Text style={cl.lbl}>Risk type</Text><Text style={cl.val}>{c.riskType}</Text></View>
          <View><Text style={cl.lbl}>Claim date</Text><Text style={cl.val}>{c.claimDate}</Text></View>
          <View>
            <Text style={cl.lbl}>Payout</Text>
            <Text style={[cl.val, {color:'#22d3ee', fontWeight:'700'}]}>₹{c.payoutAmountRupees}</Text>
          </View>
        </View>
      </View>
    );
  };

  return (
    <View style={cl.bg}>
      <View style={cl.header}>
        <TouchableOpacity onPress={()=>navigation.goBack()}><Text style={cl.back}>← Back</Text></TouchableOpacity>
        <Text style={cl.title}>Claims History</Text>
        <Text style={cl.sub}>Fully automated · zero-touch settlement</Text>
      </View>
      {isLoading ? <ActivityIndicator color="#22d3ee" style={{ marginTop:48 }}/> :
      !claims || claims.length===0 ? (
        <View style={{ alignItems:'center', marginTop:60 }}>
          <Text style={{ fontSize:48, marginBottom:12 }}>📋</Text>
          <Text style={{ color:'#64748b', fontSize:16 }}>No claims yet</Text>
          <Text style={{ color:'#475569', fontSize:13, marginTop:6 }}>Claims are auto-generated when a trigger fires</Text>
        </View>
      ) : (
        <FlatList data={claims} keyExtractor={i=>i.id.toString()} renderItem={renderItem}
          contentContainerStyle={{ padding:16 }}/>
      )}
    </View>
  );
}

const cl = StyleSheet.create({
  bg:         { flex:1, backgroundColor:'#0f172a' },
  header:     { padding:20, paddingTop:52, borderBottomWidth:1, borderBottomColor:'#1e293b' },
  back:       { color:'#22d3ee', fontSize:14, marginBottom:8 },
  title:      { fontSize:24, fontWeight:'700', color:'#f1f5f9' },
  sub:        { color:'#475569', fontSize:12, marginTop:4 },
  card:       { backgroundColor:'#1e293b', borderRadius:14, padding:16, marginBottom:12, borderWidth:1, borderColor:'#334155' },
  claimNum:   { color:'#94a3b8', fontSize:13, fontFamily:'monospace' },
  statusPill: { borderRadius:10, paddingHorizontal:8, paddingVertical:3 },
  statusTxt:  { fontSize:11, fontWeight:'700' },
  lbl:        { color:'#475569', fontSize:11 },
  val:        { color:'#f1f5f9', fontSize:14, fontWeight:'600', marginTop:2 },
});
