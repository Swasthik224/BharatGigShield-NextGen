package com.paramins.service;

import com.paramins.dto.PremiumQuoteResponse;
import com.paramins.entity.Policy;
import com.paramins.entity.User;
import org.springframework.stereotype.Service;

@Service
public class PricingEngineService {

    /**
     * Premium = TriggerProbability × AvgIncomeLoss × ExposureDays × LoadingFactor
     * Clamped to ₹20–₹50 per week (2000–5000 paise).
     * Income loss assumed ₹500/day for a gig worker.
     */
    public PremiumQuoteResponse quote(String city, Policy.RiskType riskType, User.WorkerTier tier) {
        double prob    = historicalProb(city, riskType);
        double avgLoss = 50000.0;   // ₹500 daily loss in paise
        int    days    = 7;
        double loading = loadingFor(tier);

        int premium = (int) Math.max(2000, Math.min(5000, prob * avgLoss * days * loading));

        int payout;
        switch (tier) {
            case TIER_A: payout = 50000; break;
            case TIER_B: payout = 40000; break;
            default:     payout = 30000; break;
        }

        return PremiumQuoteResponse.builder()
            .city(city)
            .riskType(riskType.name())
            .tier(tier.name())
            .weeklyPremiumPaise(premium)
            .weeklyPremiumRupees(premium / 100.0)
            .payoutPerDayPaise(payout)
            .payoutPerDayRupees(payout / 100.0)
            .triggerProbabilityPct(Math.round(prob * 100))
            .build();
    }

    private double historicalProb(String city, Policy.RiskType type) {
        String key = city.toLowerCase() + "_" + type.name();
        switch (key) {
            case "delhi_aqi":    return 0.18;
            case "delhi_heat":   return 0.08;
            case "mumbai_rain":  return 0.12;
            case "mumbai_heat":  return 0.05;
            case "chennai_rain": return 0.14;
            case "chennai_heat": return 0.10;
            case "kolkata_aqi":  return 0.15;
            case "kolkata_rain": return 0.11;
            default:             return 0.10;
        }
    }

    private double loadingFor(User.WorkerTier tier) {
        switch (tier) {
            case TIER_A: return 1.25;
            case TIER_B: return 1.30;
            default:     return 1.40;
        }
    }
}
