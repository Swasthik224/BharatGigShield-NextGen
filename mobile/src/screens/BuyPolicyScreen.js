import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator, Alert, ScrollView } from 'react-native';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '../api/client';

const TYPES = [
  { value:'AQI',  label:'Air Quality (AQI > 300)', icon:'🌫️', desc:'Triggered when AQI exceeds hazardous level' },
  { value:'RAIN', label:'Heavy Rain (> 50mm)',      icon:'🌧️', desc:'Triggered on extreme rainfall in 24 hours' },
  { value:'HEAT', label:'Heat Wave (> 42°C)',        icon:'🔥', desc:'Triggered when max temp crosses 42°C' },
];
const CITIES = ['Delhi','Mumbai','Chennai','Kolkata','Bangalore','Hyderabad'];

export default function BuyPolicyScreen({ navigation }) {
  const [riskType, setRiskType] = useState('AQI');
  const [city,     setCity]     = useState('Delhi');
  const qc = useQueryClient();

  const { data: quote, isFetching } = useQuery({
    queryKey: ['quote', city, riskType],
    queryFn: () => api.get(`/policies/quote?city=${city}&riskType=${riskType}`).then(r=>r.data.data),
  });

  const enroll = useMutation({
    mutationFn: () => api.post('/policies/enroll', { city, riskType }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['my-policies'] });
      Alert.alert('🎉 Policy Activated!', 'Your coverage starts immediately. Payouts are automatic — no claim filing needed.', [
        { text: 'Great!', onPress: () => navigation.goBack() }
      ]);
    },
    onError: e => Alert.alert('Enrollment Failed', e.response?.data?.message || 'Please try again'),
  });

  return (
    <ScrollView style={b.bg}>
      <View style={b.header}>
        <TouchableOpacity onPress={()=>navigation.goBack()} style={b.back}>
          <Text style={b.backTxt}>← Back</Text>
        </TouchableOpacity>
        <Text style={b.title}>Choose Coverage</Text>
      </View>

      <Text style={b.sectionLabel}>COVERAGE TYPE</Text>
      {TYPES.map(t=>(
        <TouchableOpacity key={t.value} style={[b.typeCard, riskType===t.value&&b.typeCardActive]}
          onPress={()=>setRiskType(t.value)}>
          <Text style={b.typeIcon}>{t.icon}</Text>
          <View style={{ flex:1 }}>
            <Text style={[b.typeLabel, riskType===t.value&&{color:'#22d3ee'}]}>{t.label}</Text>
            <Text style={b.typeDesc}>{t.desc}</Text>
          </View>
          {riskType===t.value && <Text style={{ color:'#22d3ee', fontSize:18 }}>✓</Text>}
        </TouchableOpacity>
      ))}

      <Text style={b.sectionLabel}>CITY</Text>
      <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ paddingHorizontal:16, marginBottom:12 }}>
        {CITIES.map(c=>(
          <TouchableOpacity key={c} onPress={()=>setCity(c)}
            style={[b.cityChip, city===c&&b.cityChipActive]}>
            <Text style={{ color: city===c?'#22d3ee':'#64748b', fontWeight:'600', fontSize:13 }}>{c}</Text>
          </TouchableOpacity>
        ))}
      </ScrollView>

      {isFetching && <ActivityIndicator color="#22d3ee" style={{ margin:20 }}/>}

      {quote && !isFetching && (
        <View style={b.quoteCard}>
          <Text style={b.quoteTitle}>Your Personalized Quote</Text>
          {[
            ['Weekly premium',          `₹${quote.weeklyPremiumRupees}`],
            ['Payout per trigger day',  `₹${quote.payoutPerDayRupees}`],
            ['Coverage tier',           quote.tier],
            ['Historical trigger rate', `${quote.triggerProbabilityPct}% per year`],
          ].map(([label, val])=>(
            <View key={label} style={b.quoteRow}>
              <Text style={b.quoteLabel}>{label}</Text>
              <Text style={b.quoteVal}>{val}</Text>
            </View>
          ))}
          <View style={b.divider}/>
          <View style={{ flexDirection:'row', flexWrap:'wrap', gap:8 }}>
            {['✅ Zero paperwork','⚡ Auto payout in minutes','🔒 No claim filing'].map(t=>(
              <Text key={t} style={b.pill}>{t}</Text>
            ))}
          </View>
        </View>
      )}

      <TouchableOpacity style={[b.cta, (!quote||enroll.isPending)&&{opacity:0.6}]}
        onPress={()=>enroll.mutate()} disabled={!quote||enroll.isPending}>
        {enroll.isPending ? <ActivityIndicator color="#0f172a"/> :
          <Text style={b.ctaTxt}>Activate for ₹{quote?.weeklyPremiumRupees??'…'}/week</Text>}
      </TouchableOpacity>
      <View style={{ height:48 }}/>
    </ScrollView>
  );
}

const b = StyleSheet.create({
  bg:           { flex:1, backgroundColor:'#0f172a' },
  header:       { padding:20, paddingTop:52 },
  back:         { marginBottom:8 },
  backTxt:      { color:'#22d3ee', fontSize:14 },
  title:        { fontSize:28, fontWeight:'700', color:'#f1f5f9' },
  sectionLabel: { color:'#475569', fontSize:11, letterSpacing:1, marginHorizontal:16, marginTop:20, marginBottom:10 },
  typeCard:     { flexDirection:'row', alignItems:'center', gap:14, backgroundColor:'#1e293b', marginHorizontal:16, marginBottom:10, borderRadius:14, padding:16, borderWidth:1, borderColor:'#334155' },
  typeCardActive:{ borderColor:'#22d3ee', backgroundColor:'#0e3a4a' },
  typeIcon:     { fontSize:26 },
  typeLabel:    { color:'#94a3b8', fontSize:15, fontWeight:'600' },
  typeDesc:     { color:'#475569', fontSize:12, marginTop:2 },
  cityChip:     { backgroundColor:'#1e293b', borderRadius:20, paddingHorizontal:14, paddingVertical:8, marginRight:8, borderWidth:1, borderColor:'#334155' },
  cityChipActive:{ borderColor:'#22d3ee', backgroundColor:'#0e3a4a' },
  quoteCard:    { backgroundColor:'#1e293b', margin:16, borderRadius:16, padding:18 },
  quoteTitle:   { color:'#64748b', fontSize:11, letterSpacing:1, textTransform:'uppercase', marginBottom:14 },
  quoteRow:     { flexDirection:'row', justifyContent:'space-between', marginBottom:12 },
  quoteLabel:   { color:'#64748b', fontSize:14 },
  quoteVal:     { color:'#f1f5f9', fontSize:15, fontWeight:'700' },
  divider:      { height:1, backgroundColor:'#334155', marginVertical:14 },
  pill:         { backgroundColor:'#1e293b55', borderWidth:1, borderColor:'#334155', borderRadius:20, paddingHorizontal:10, paddingVertical:4, color:'#64748b', fontSize:12 },
  cta:          { backgroundColor:'#22d3ee', borderRadius:14, padding:18, alignItems:'center', margin:16 },
  ctaTxt:       { color:'#0f172a', fontWeight:'700', fontSize:17 },
});
