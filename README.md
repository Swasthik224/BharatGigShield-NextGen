# GigShield — Parametric Insurance for Gig Workers

Zero-touch parametric insurance that auto-pays workers when AQI > 300, Rain > 50mm, or Temperature > 42°C.

## Project Structure

```
gigshield/
├── backend/          Spring Boot 3.2 (Java 17)
├── frontend/         React 18 Admin Dashboard
├── mobile/           React Native (Expo) Mobile App
└── schema.sql        MySQL database schema
```

---

## Quick Start

### 1. MySQL Setup
```sql
-- Run schema.sql first:
mysql -u root -p < schema.sql
```
Or let Spring Boot auto-create with `ddl-auto: update` (default in application.yml).

### 2. Backend
```bash
cd backend
# Edit src/main/resources/application.yml — set DB password
mvn spring-boot:run
# Starts on http://localhost:8080
```
OTPs print to console (replace with SMS in production).

### 3. Frontend (Admin Dashboard)
```bash
cd frontend
npm install
npm start
# Opens http://localhost:3000
# Login with any phone → check backend console for OTP
```

### 4. Mobile App
```bash
cd mobile
npm install
npx expo start
# Scan QR with Expo Go app
# For Android emulator: BASE_URL is already set to 10.0.2.2:8080
```

---

## Key API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/otp/send` | Send OTP |
| POST | `/api/v1/auth/otp/verify` | Verify OTP → JWT |
| POST | `/api/v1/auth/register` | Register worker |
| GET  | `/api/v1/policies/quote?city=Delhi&riskType=AQI` | Get premium quote |
| POST | `/api/v1/policies/enroll` | Buy policy |
| GET  | `/api/v1/policies/my` | My policies |
| GET  | `/api/v1/claims/my` | My claims |
| POST | `/api/v1/activity/seed-test-data` | Seed 15 activity days (dev) |
| GET  | `/api/v1/admin/triggers/recent` | Recent trigger events |
| GET  | `/api/v1/admin/claims/stats` | Claim statistics |
| POST | `/api/v1/admin/triggers/simulate` | Fire a manual trigger (dev) |

---

## End-to-End Flow

```
1. Worker registers → 4-step onboarding
2. Buys AQI/Rain/Heat policy → ₹20-50/week premium
3. Trigger Engine polls CPCB/IMD every 15 min
4. Threshold breached → TriggerEvent created
5. Claim Engine finds all active matching policies
6. Per-worker: activity check → fraud check → auto-approve
7. PayoutService sends UPI payment (mock: logs to console)
8. Worker notified via SMS/push (mock: console log)
```

## Pricing Formula
```
Premium = TriggerProbability × ₹500 × 7 days × LoadingFactor
Loading: TIER_A=1.25x, TIER_B=1.30x, TIER_C=1.40x
Weekly range: ₹20–₹50

BCR = Total Claims / Total Premium  [target: 0.55–0.70]
Suspend enrollments if BCR > 85%
```

## Replacing Mock Components

| Mock | Production Replacement |
|------|----------------------|
| Console OTP | MSG91 / Firebase Auth |
| `CpcbApiClient` | `GET https://api.cpcb.gov.in/aqi` |
| `ImdApiClient`  | `GET https://api.imd.gov.in/weather` |
| `mockUpiPayout` | NPCI UPI API / Razorpay Payouts API |
| Console notifications | Firebase FCM + Twilio SMS |

## Assumptions
- All monetary values stored in paise (₹1 = 100 paise)
- UTC timestamps throughout
- Underwriting requires ≥7 active platform days in last 30 days
- Fraud score > 0.70 rejects the claim
- GPS matching uses gps_city field from ActivityLog vs policy city
