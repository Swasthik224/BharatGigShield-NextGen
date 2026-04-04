package com.paramins.dto;

public class PremiumQuoteResponse {
    private String city, riskType, tier;
    private int weeklyPremiumPaise, payoutPerDayPaise;
    private double weeklyPremiumRupees, payoutPerDayRupees;
    private long triggerProbabilityPct;

    public PremiumQuoteResponse() {}

    private PremiumQuoteResponse(Builder b) {
        this.city = b.city; this.riskType = b.riskType; this.tier = b.tier;
        this.weeklyPremiumPaise = b.weeklyPremiumPaise; this.payoutPerDayPaise = b.payoutPerDayPaise;
        this.weeklyPremiumRupees = b.weeklyPremiumRupees; this.payoutPerDayRupees = b.payoutPerDayRupees;
        this.triggerProbabilityPct = b.triggerProbabilityPct;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String city, riskType, tier;
        private int weeklyPremiumPaise, payoutPerDayPaise;
        private double weeklyPremiumRupees, payoutPerDayRupees;
        private long triggerProbabilityPct;

        public Builder city(String v)                   { this.city = v; return this; }
        public Builder riskType(String v)               { this.riskType = v; return this; }
        public Builder tier(String v)                   { this.tier = v; return this; }
        public Builder weeklyPremiumPaise(int v)        { this.weeklyPremiumPaise = v; return this; }
        public Builder payoutPerDayPaise(int v)         { this.payoutPerDayPaise = v; return this; }
        public Builder weeklyPremiumRupees(double v)    { this.weeklyPremiumRupees = v; return this; }
        public Builder payoutPerDayRupees(double v)     { this.payoutPerDayRupees = v; return this; }
        public Builder triggerProbabilityPct(long v)    { this.triggerProbabilityPct = v; return this; }
        public PremiumQuoteResponse build()             { return new PremiumQuoteResponse(this); }
    }

    public String getCity()                     { return city; }
    public String getRiskType()                 { return riskType; }
    public String getTier()                     { return tier; }
    public int getWeeklyPremiumPaise()          { return weeklyPremiumPaise; }
    public int getPayoutPerDayPaise()           { return payoutPerDayPaise; }
    public double getWeeklyPremiumRupees()      { return weeklyPremiumRupees; }
    public double getPayoutPerDayRupees()       { return payoutPerDayRupees; }
    public long getTriggerProbabilityPct()      { return triggerProbabilityPct; }
}
