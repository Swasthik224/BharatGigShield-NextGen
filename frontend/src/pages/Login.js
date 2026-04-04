import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/client';

export default function Login() {
  const [phone, setPhone] = useState('');
  const [otp, setOtp] = useState('');
  const [step, setStep] = useState('phone');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // 🔹 Send OTP
  const sendOtp = async () => {
    setLoading(true);
    setError('');
    try {
      await api.post('/auth/otp/send', { phone });
      setStep('otp');
    } catch (e) {
      setError(e.response?.data?.message || 'Failed to send OTP');
    } finally {
      setLoading(false);
    }
  };

  // 🔹 Verify OTP (FIXED)
  const verify = async () => {
  setLoading(true);
  setError('');

  try {
    console.log("STEP 1: Sending request...");

    const { data } = await api.post('/auth/otp/verify', { phone, otp });

    console.log("STEP 2: Raw response:", data);

    const res = data?.data;

    console.log("STEP 3: Extracted:", JSON.stringify(res, null, 2));

    if (!res || !res.role) {
      console.log("STEP 4: ROLE MISSING ❌");
      setError("Role not received from server");
      return;
    }

    console.log("STEP 5: ROLE =", res.role);

    localStorage.setItem('jwt_token', res.token);
    localStorage.setItem('role', res.role);

    if (res.role === 'ADMIN') {
      navigate('/admin', { replace: true });
    } else {
      navigate('/user', { replace: true });
    }

  } catch (e) {
    console.error("STEP ERROR:", e);
    setError("OTP verification failed");
  } finally {
    setLoading(false);
  }
};

  const styles = {
    wrap: {
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: '#0f172a'
    },
    card: {
      background: '#1e293b',
      borderRadius: 16,
      padding: 40,
      width: 380,
      border: '1px solid #334155'
    },
    brand: {
      fontSize: 32,
      fontWeight: 800,
      color: '#22d3ee',
      marginBottom: 4
    },
    sub: {
      color: '#64748b',
      fontSize: 13,
      marginBottom: 36
    },
    label: {
      color: '#94a3b8',
      fontSize: 13,
      marginBottom: 8,
      display: 'block'
    },
    input: {
      width: '100%',
      background: '#0f172a',
      border: '1px solid #334155',
      borderRadius: 10,
      padding: '12px 14px',
      color: '#f1f5f9',
      fontSize: 16,
      marginBottom: 16
    },
    btn: {
      width: '100%',
      background: '#22d3ee',
      border: 'none',
      borderRadius: 10,
      padding: '14px',
      color: '#0f172a',
      fontWeight: 700,
      cursor: 'pointer'
    },
    err: {
      color: '#f87171',
      fontSize: 13,
      marginBottom: 12
    },
    back: {
      color: '#22d3ee',
      fontSize: 13,
      marginTop: 12,
      cursor: 'pointer',
      textAlign: 'center'
    }
  };

  return (
    <div style={styles.wrap}>
      <div style={styles.card}>
        <div style={styles.brand}>GigShield</div>
        <div style={styles.sub}>Login to GigShield</div>

        {error && <div style={styles.err}>{error}</div>}

        {step === 'phone' ? (
          <>
            <label style={styles.label}>Phone Number</label>
            <input
              style={styles.input}
              type="tel"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />

            <button style={styles.btn} onClick={sendOtp} disabled={loading}>
              {loading ? 'Sending...' : 'Send OTP'}
            </button>
          </>
        ) : (
          <>
            <label style={styles.label}>Enter OTP</label>
            <input
              style={styles.input}
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              maxLength={6}
            />

            <button style={styles.btn} onClick={verify} disabled={loading}>
              {loading ? 'Verifying...' : 'Verify & Login'}
            </button>

            <div style={styles.back} onClick={() => setStep('phone')}>
              ← Change number
            </div>
          </>
        )}
      </div>
    </div>
  );
}