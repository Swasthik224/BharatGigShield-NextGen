package com.paramins.dto;

import com.paramins.entity.Policy;
import java.time.LocalDate;

public class PolicyDto {
    private Long id;
    private String policyNumber, riskType, city, status;
    private double weeklyPremiumRupees, payoutPerDayRupees;
    private LocalDate nextBillingDate, startDate, endDate;

    public PolicyDto() {}

    private PolicyDto(Builder b) {
        this.id = b.id; this.policyNumber = b.policyNumber; this.riskType = b.riskType;
        this.city = b.city; this.status = b.status;
        this.weeklyPremiumRupees = b.weeklyPremiumRupees; this.payoutPerDayRupees = b.payoutPerDayRupees;
        this.nextBillingDate = b.nextBillingDate; this.startDate = b.startDate; this.endDate = b.endDate;
    }

    public static PolicyDto from(Policy p) {
        return new Builder()
            .id(p.getId()).policyNumber(p.getPolicyNumber())
            .riskType(p.getRiskType().name()).city(p.getCity()).status(p.getStatus().name())
            .weeklyPremiumRupees(p.getWeeklyPremium() / 100.0)
            .payoutPerDayRupees(p.getPayoutPerDay() / 100.0)
            .nextBillingDate(p.getNextBillingDate())
            .startDate(p.getStartDate()).endDate(p.getEndDate())
            .build();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String policyNumber, riskType, city, status;
        private double weeklyPremiumRupees, payoutPerDayRupees;
        private LocalDate nextBillingDate, startDate, endDate;

        public Builder id(Long v)                       { this.id = v; return this; }
        public Builder policyNumber(String v)           { this.policyNumber = v; return this; }
        public Builder riskType(String v)               { this.riskType = v; return this; }
        public Builder city(String v)                   { this.city = v; return this; }
        public Builder status(String v)                 { this.status = v; return this; }
        public Builder weeklyPremiumRupees(double v)    { this.weeklyPremiumRupees = v; return this; }
        public Builder payoutPerDayRupees(double v)     { this.payoutPerDayRupees = v; return this; }
        public Builder nextBillingDate(LocalDate v)     { this.nextBillingDate = v; return this; }
        public Builder startDate(LocalDate v)           { this.startDate = v; return this; }
        public Builder endDate(LocalDate v)             { this.endDate = v; return this; }
        public PolicyDto build()                        { return new PolicyDto(this); }
    }

    public Long getId()                     { return id; }
    public String getPolicyNumber()         { return policyNumber; }
    public String getRiskType()             { return riskType; }
    public String getCity()                 { return city; }
    public String getStatus()               { return status; }
    public double getWeeklyPremiumRupees()  { return weeklyPremiumRupees; }
    public double getPayoutPerDayRupees()   { return payoutPerDayRupees; }
    public LocalDate getNextBillingDate()   { return nextBillingDate; }
    public LocalDate getStartDate()         { return startDate; }
    public LocalDate getEndDate()           { return endDate; }
}
