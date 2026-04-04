package com.paramins.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
public class Policy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true, length = 30)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type", nullable = false)
    private RiskType riskType;

    @Column(nullable = false, length = 60)
    private String city;

    private String ward;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status = PolicyStatus.ACTIVE;

    @Column(name = "weekly_premium", nullable = false)
    private Integer weeklyPremium;

    @Column(name = "payout_per_day", nullable = false)
    private Integer payoutPerDay;

    @Column(name = "trigger_threshold", nullable = false, precision = 8, scale = 2)
    private BigDecimal triggerThreshold;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "next_billing_date")
    private LocalDate nextBillingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  void onUpdate() { updatedAt = LocalDateTime.now(); }

    public enum RiskType    { AQI, RAIN, HEAT }
    public enum PolicyStatus { ACTIVE, SUSPENDED, EXPIRED, CANCELLED }

    // ── Constructors ─────────────────────────────────────────
    public Policy() {}

    private Policy(Builder b) {
        this.policyNumber = b.policyNumber; this.user = b.user; this.riskType = b.riskType;
        this.city = b.city; this.ward = b.ward; this.status = b.status;
        this.weeklyPremium = b.weeklyPremium; this.payoutPerDay = b.payoutPerDay;
        this.triggerThreshold = b.triggerThreshold; this.startDate = b.startDate;
        this.endDate = b.endDate; this.nextBillingDate = b.nextBillingDate;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String policyNumber, city, ward;
        private User user;
        private RiskType riskType;
        private PolicyStatus status = PolicyStatus.ACTIVE;
        private Integer weeklyPremium, payoutPerDay;
        private BigDecimal triggerThreshold;
        private LocalDate startDate, endDate, nextBillingDate;

        public Builder policyNumber(String v)       { this.policyNumber = v; return this; }
        public Builder user(User v)                 { this.user = v; return this; }
        public Builder riskType(RiskType v)         { this.riskType = v; return this; }
        public Builder city(String v)               { this.city = v; return this; }
        public Builder ward(String v)               { this.ward = v; return this; }
        public Builder status(PolicyStatus v)       { this.status = v; return this; }
        public Builder weeklyPremium(Integer v)     { this.weeklyPremium = v; return this; }
        public Builder payoutPerDay(Integer v)      { this.payoutPerDay = v; return this; }
        public Builder triggerThreshold(BigDecimal v){ this.triggerThreshold = v; return this; }
        public Builder startDate(LocalDate v)       { this.startDate = v; return this; }
        public Builder endDate(LocalDate v)         { this.endDate = v; return this; }
        public Builder nextBillingDate(LocalDate v) { this.nextBillingDate = v; return this; }
        public Policy build()                       { return new Policy(this); }
    }

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                         { return id; }
    public void setId(Long id)                  { this.id = id; }
    public String getPolicyNumber()             { return policyNumber; }
    public void setPolicyNumber(String v)       { this.policyNumber = v; }
    public User getUser()                       { return user; }
    public void setUser(User v)                 { this.user = v; }
    public RiskType getRiskType()               { return riskType; }
    public void setRiskType(RiskType v)         { this.riskType = v; }
    public String getCity()                     { return city; }
    public void setCity(String v)               { this.city = v; }
    public String getWard()                     { return ward; }
    public void setWard(String v)               { this.ward = v; }
    public PolicyStatus getStatus()             { return status; }
    public void setStatus(PolicyStatus v)       { this.status = v; }
    public Integer getWeeklyPremium()           { return weeklyPremium; }
    public void setWeeklyPremium(Integer v)     { this.weeklyPremium = v; }
    public Integer getPayoutPerDay()            { return payoutPerDay; }
    public void setPayoutPerDay(Integer v)      { this.payoutPerDay = v; }
    public BigDecimal getTriggerThreshold()     { return triggerThreshold; }
    public void setTriggerThreshold(BigDecimal v){ this.triggerThreshold = v; }
    public LocalDate getStartDate()             { return startDate; }
    public void setStartDate(LocalDate v)       { this.startDate = v; }
    public LocalDate getEndDate()               { return endDate; }
    public void setEndDate(LocalDate v)         { this.endDate = v; }
    public LocalDate getNextBillingDate()       { return nextBillingDate; }
    public void setNextBillingDate(LocalDate v) { this.nextBillingDate = v; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public LocalDateTime getUpdatedAt()         { return updatedAt; }
}
