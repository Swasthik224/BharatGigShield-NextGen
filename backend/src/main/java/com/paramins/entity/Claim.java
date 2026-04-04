package com.paramins.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_number", nullable = false, unique = true, length = 30)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_event_id", nullable = false)
    private TriggerEvent triggerEvent;

    @Column(name = "claim_date")
    private LocalDate claimDate;

    @Column(name = "trigger_days")
    private Integer triggerDays = 1;

    @Column(name = "payout_amount", nullable = false)
    private Integer payoutAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status = ClaimStatus.PENDING;

    @Column(name = "gps_verified")
    private Boolean gpsVerified;

    @Column(name = "platform_active")
    private Boolean platformActive;

    @Column(name = "fraud_score", precision = 3, scale = 2)
    private BigDecimal fraudScore;

    @Column(name = "fraud_note", length = 500)
    private String fraudNote;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  void onUpdate() { updatedAt = LocalDateTime.now(); }

    public enum ClaimStatus { PENDING, FRAUD_CHECK, APPROVED, PAID, REJECTED, REVERSED }

    // ── Constructors ─────────────────────────────────────────
    public Claim() {}

    private Claim(Builder b) {
        this.claimNumber = b.claimNumber; this.policy = b.policy; this.user = b.user;
        this.triggerEvent = b.triggerEvent; this.claimDate = b.claimDate;
        this.triggerDays = b.triggerDays; this.payoutAmount = b.payoutAmount;
        this.status = b.status;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String claimNumber, fraudNote;
        private Policy policy;
        private User user;
        private TriggerEvent triggerEvent;
        private LocalDate claimDate;
        private Integer triggerDays = 1;
        private Integer payoutAmount;
        private ClaimStatus status = ClaimStatus.PENDING;

        public Builder claimNumber(String v)        { this.claimNumber = v; return this; }
        public Builder policy(Policy v)             { this.policy = v; return this; }
        public Builder user(User v)                 { this.user = v; return this; }
        public Builder triggerEvent(TriggerEvent v) { this.triggerEvent = v; return this; }
        public Builder claimDate(LocalDate v)       { this.claimDate = v; return this; }
        public Builder triggerDays(Integer v)       { this.triggerDays = v; return this; }
        public Builder payoutAmount(Integer v)      { this.payoutAmount = v; return this; }
        public Builder status(ClaimStatus v)        { this.status = v; return this; }
        public Claim build()                        { return new Claim(this); }
    }

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                         { return id; }
    public void setId(Long v)                   { this.id = v; }
    public String getClaimNumber()              { return claimNumber; }
    public void setClaimNumber(String v)        { this.claimNumber = v; }
    public Policy getPolicy()                   { return policy; }
    public void setPolicy(Policy v)             { this.policy = v; }
    public User getUser()                       { return user; }
    public void setUser(User v)                 { this.user = v; }
    public TriggerEvent getTriggerEvent()       { return triggerEvent; }
    public void setTriggerEvent(TriggerEvent v) { this.triggerEvent = v; }
    public LocalDate getClaimDate()             { return claimDate; }
    public void setClaimDate(LocalDate v)       { this.claimDate = v; }
    public Integer getTriggerDays()             { return triggerDays; }
    public void setTriggerDays(Integer v)       { this.triggerDays = v; }
    public Integer getPayoutAmount()            { return payoutAmount; }
    public void setPayoutAmount(Integer v)      { this.payoutAmount = v; }
    public ClaimStatus getStatus()              { return status; }
    public void setStatus(ClaimStatus v)        { this.status = v; }
    public Boolean getGpsVerified()             { return gpsVerified; }
    public void setGpsVerified(Boolean v)       { this.gpsVerified = v; }
    public Boolean getPlatformActive()          { return platformActive; }
    public void setPlatformActive(Boolean v)    { this.platformActive = v; }
    public BigDecimal getFraudScore()           { return fraudScore; }
    public void setFraudScore(BigDecimal v)     { this.fraudScore = v; }
    public String getFraudNote()                { return fraudNote; }
    public void setFraudNote(String v)          { this.fraudNote = v; }
    public LocalDateTime getSettledAt()         { return settledAt; }
    public void setSettledAt(LocalDateTime v)   { this.settledAt = v; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public LocalDateTime getUpdatedAt()         { return updatedAt; }
}
