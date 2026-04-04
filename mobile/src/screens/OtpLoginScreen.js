import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ActivityIndicator, Alert, KeyboardAvoidingView, Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import api from '../api/client';

export default function OtpLoginScreen({ navigation }) {
  const [phone,   setPhone]   = useState('');
  const [otp,     setOtp]     = useState('');
  const [step,    setStep]    = useState('phone');
  const [loading, setLoading] = useState(false);

  const sendOtp = async () => {
    if (!/^[6-9]\d{9}$/.test(phone)) { Alert.alert('Enter a valid 10-digit mobile number'); return; }
    setLoading(true);
    try {
      await api.post('/auth/otp/send', { phone });
      setStep('otp');
      Alert.alert('OTP Sent', 'Check the Spring Boot server console for your OTP.');
    } catch (e) { Alert.alert('Error', e.response?.data?.message || 'Failed to send OTP'); }
    finally { setLoading(false); }
  };

  const verifyOtp = async () => {
    setLoading(true);
    try {
      const { data } = await api.post('/auth/otp/verify', { phone, otp });
      await AsyncStorage.setItem('jwt_token', data.data.token);
      if (data.data.isNew) navigation.replace('Register', { phone });
      else navigation.replace('Home');
    } catch (e) { Alert.alert('Error', e.response?.data?.message || 'OTP verification failed'); }
    finally { setLoading(false); }
  };

  return (
    <KeyboardAvoidingView behavior={Platform.OS==='ios'?'padding':'height'} style={s.container}>
      <View style={s.inner}>
        <Text style={s.brand}>GigShield</Text>
        <Text style={s.tagline}>Insurance for every delivery</Text>

        {step === 'phone' ? (
          <>
            <Text style={s.label}>Mobile Number</Text>
            <View style={s.row}>
              <Text style={s.prefix}>+91</Text>
              <TextInput style={s.input} keyboardType="phone-pad" maxLength={10}
                value={phone} onChangeText={setPhone} placeholder="9876543210" placeholderTextColor="#475569"/>
            </View>
            <TouchableOpacity style={s.btn} onPress={sendOtp} disabled={loading}>
              {loading ? <ActivityIndicator color="#0f172a"/> : <Text style={s.btnTxt}>Get OTP →</Text>}
            </TouchableOpacity>
          </>
        ) : (
          <>
            <Text style={s.label}>Enter OTP (check server console)</Text>
            <TextInput style={[s.input, {textAlign:'center',letterSpacing:10,fontSize:26,paddingVertical:16}]}
              keyboardType="number-pad" maxLength={6} value={otp} onChangeText={setOtp}/>
            <TouchableOpacity style={s.btn} onPress={verifyOtp} disabled={loading}>
              {loading ? <ActivityIndicator color="#0f172a"/> : <Text style={s.btnTxt}>Verify & Login</Text>}
            </TouchableOpacity>
            <TouchableOpacity onPress={()=>{setStep('phone');setOtp('');}}>
              <Text style={s.link}>← Change number</Text>
            </TouchableOpacity>
          </>
        )}
      </View>
    </KeyboardAvoidingView>
  );
}

const s = StyleSheet.create({
  container: { flex:1, backgroundColor:'#0f172a' },
  inner:     { flex:1, padding:24, justifyContent:'center' },
  brand:     { fontSize:40, fontWeight:'800', color:'#22d3ee', textAlign:'center', marginBottom:6 },
  tagline:   { fontSize:14, color:'#64748b', textAlign:'center', marginBottom:52 },
  label:     { color:'#94a3b8', fontSize:14, marginBottom:10 },
  row:       { flexDirection:'row', alignItems:'center', marginBottom:18 },
  prefix:    { color:'#94a3b8', fontSize:16, marginRight:10, paddingVertical:14 },
  input:     { flex:1, backgroundColor:'#1e293b', color:'#f1f5f9', padding:14, borderRadius:12, fontSize:18, borderWidth:1, borderColor:'#334155' },
  btn:       { backgroundColor:'#22d3ee', padding:16, borderRadius:12, alignItems:'center', marginTop:4 },
  btnTxt:    { color:'#0f172a', fontWeight:'700', fontSize:16 },
  link:      { color:'#22d3ee', marginTop:18, textAlign:'center', fontSize:14 },
});
