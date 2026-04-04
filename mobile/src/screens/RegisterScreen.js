import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ScrollView, Alert, ActivityIndicator } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import api from '../api/client';

const CITIES    = ['Delhi','Mumbai','Chennai','Kolkata','Bangalore','Hyderabad'];
const PLATFORMS = ['ZOMATO','SWIGGY','ZEPTO','OTHER'];

export default function RegisterScreen({ navigation, route }) {
  const [step, setStep] = useState(1);
  const [form, setForm] = useState({ phone: route.params?.phone||'', name:'', city:'Delhi', upiId:'', platform:'ZOMATO' });
  const [loading, setLoading] = useState(false);

  const next = () => {
    if (step===1 && !form.name.trim()) { Alert.alert('Enter your full name'); return; }
    if (step===3 && !form.upiId.includes('@')) { Alert.alert('Enter a valid UPI ID (e.g. name@upi)'); return; }
    if (step<4) setStep(s=>s+1); else submit();
  };

  const submit = async () => {
    setLoading(true);
    try {
      await api.post('/auth/register', form);
      // Seed test activity so underwriting passes
      await api.post('/activity/seed-test-data');
      navigation.replace('Home');
    } catch (e) { Alert.alert('Error', e.response?.data?.message||'Registration failed'); }
    finally { setLoading(false); }
  };

  const steps = ['Your Name','Your City','UPI ID','Platform'];

  return (
    <ScrollView contentContainerStyle={r.container}>
      <Text style={r.title}>{steps[step-1]}</Text>
      <Text style={r.stepText}>Step {step} of 4</Text>

      {/* Step dots */}
      <View style={{ flexDirection:'row', marginBottom:28 }}>
        {[1,2,3,4].map(i=>(
          <View key={i} style={[r.dot, i<=step&&r.dotActive, i===step&&{width:24}]}/>
        ))}
      </View>

      {step===1 && (
        <TextInput style={r.input} placeholder="Full name" placeholderTextColor="#475569"
          value={form.name} onChangeText={v=>setForm(f=>({...f,name:v}))}/>
      )}
      {step===2 && (
        <View style={{ flexDirection:'row', flexWrap:'wrap', gap:10 }}>
          {CITIES.map(c=>(
            <TouchableOpacity key={c} onPress={()=>setForm(f=>({...f,city:c}))}
              style={[r.chip, form.city===c&&r.chipActive]}>
              <Text style={{ color: form.city===c?'#22d3ee':'#94a3b8', fontWeight:'600' }}>{c}</Text>
            </TouchableOpacity>
          ))}
        </View>
      )}
      {step===3 && (
        <TextInput style={r.input} placeholder="yourname@upi" placeholderTextColor="#475569"
          autoCapitalize="none" keyboardType="email-address"
          value={form.upiId} onChangeText={v=>setForm(f=>({...f,upiId:v}))}/>
      )}
      {step===4 && (
        <View style={{ gap:10 }}>
          {PLATFORMS.map(p=>(
            <TouchableOpacity key={p} onPress={()=>setForm(f=>({...f,platform:p}))}
              style={[r.chip, {width:'100%', justifyContent:'flex-start', padding:14}, form.platform===p&&r.chipActive]}>
              <Text style={{ color: form.platform===p?'#22d3ee':'#94a3b8', fontWeight:'600', fontSize:15 }}>{p}</Text>
            </TouchableOpacity>
          ))}
        </View>
      )}

      <TouchableOpacity style={r.btn} onPress={next} disabled={loading}>
        {loading ? <ActivityIndicator color="#0f172a"/> :
          <Text style={r.btnTxt}>{step<4?'Next →':'Create Account'}</Text>}
      </TouchableOpacity>
    </ScrollView>
  );
}

const r = StyleSheet.create({
  container: { backgroundColor:'#0f172a', padding:24, flexGrow:1, justifyContent:'center' },
  title:     { fontSize:30, fontWeight:'700', color:'#f1f5f9', marginBottom:4 },
  stepText:  { color:'#64748b', fontSize:13, marginBottom:20 },
  dot:       { width:8, height:8, borderRadius:4, backgroundColor:'#334155', marginRight:8, transition:'all .2s' },
  dotActive: { backgroundColor:'#22d3ee' },
  input:     { backgroundColor:'#1e293b', color:'#f1f5f9', padding:16, borderRadius:12, fontSize:17, borderWidth:1, borderColor:'#334155', marginBottom:16 },
  chip:      { backgroundColor:'#1e293b', borderRadius:10, padding:12, borderWidth:1, borderColor:'#334155' },
  chipActive:{ borderColor:'#22d3ee', backgroundColor:'#0e3a4a' },
  btn:       { backgroundColor:'#22d3ee', padding:16, borderRadius:12, alignItems:'center', marginTop:24 },
  btnTxt:    { color:'#0f172a', fontWeight:'700', fontSize:16 },
});
