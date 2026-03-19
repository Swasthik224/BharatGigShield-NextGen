GigShield: AI-Powered Parametric Insurance for India’s Gig Economy

Team Name: nextgencoders
Target Users: Q-Commerce & Food Delivery Partners (Swiggy, Zomato, Zepto, Blinkit)
Mission: Protect gig workers from income loss due to external disruptions

 1. Problem Statement

India’s delivery partners lose 20–30% of their monthly income due to:

 Environmental Disruptions: Rain, floods, heatwaves

 Social Disruptions: Curfews, strikes, zone shutdowns

 Insurance Gap: No coverage for loss of working hours

 Existing insurance only covers accidents/life — not income loss

 2. User Persona: Ravi the Rider
Feature	Details
Name	Ravi Kumar
Age / Location	26, Hyderabad (Hitech City / Gachibowli)
Platform	Zepto / Blinkit
Daily Earnings	₹700 – ₹900
Pain Point	Rain stops work → misses bonus → loses ~30% income
Tech Constraint	Budget Android, needs simple voice-based UI
 3. Solution: GigShield

GigShield = AI-driven parametric income insurance

 Instant payouts (no claims)

 Auto-trigger using APIs (weather/location)

 Based on real work activity

Weekly subscription (₹25/week)

 Voice-first interaction

4. Key Innovations
 4.1 Weekly Micro-Premium Model

Subscription deducted weekly

AI pricing using Random Forest Regressor

Based on weather + trust score

 4.2 Tap-to-Talk Interface

One-tap voice reporting

Example: “Road blocked by protest”

AI verifies using:

Ambient sound (rain/crowd)

GPS + context

 4.3 AI Fraud Detection

Isolation Forest Model detects:

GPS spoofing

Fake activity

Validates:

Active work session

Real presence

 5. System Architecture
 6. Tech Stack

Frontend: ⚛️ React Native (Expo / CLI)

Lightweight for low-end Android

Voice-first UX support

Cross-platform ready

Backend: ☕ Spring Boot (Java)

AI/ML: 🐍 Python (Scikit-learn)

APIs:

OpenWeather (weather triggers)

Google Maps (location tracking)

Razorpay / UPI (instant payouts)

 7. Workflow (Ravi’s Journey)

Onboarding: KYC + ₹25 premium

Tracking: Background activity tracking

Trigger:

Auto → Severe weather detected

Manual → Voice report

Verification: AI validates activity + location

Payout: Instant UPI compensation

 8. Policy Terms

Insured Event: >60 min disruption

Payout: Fixed hourly compensation

Verification: Weather API + GPS data

Termination:

Missed premium OR

Fraud score >80%

9. Roadmap

Week 1–2: Research & persona design

Week 3–4: Parametric engine + pricing

Week 5–6: Fraud detection + payouts

 Final Pitch

 “GigShield delivers instant, claim-free income protection for gig workers using AI-driven parametric insurance and a simple weekly subscription—powered by a lightweight React Native app.”
