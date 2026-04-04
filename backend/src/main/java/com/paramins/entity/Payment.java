package com.paramins.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id", nullable = false, unique = true, length = 40)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, length = 5)
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Column(name = "channel_ref")
    private String channelRef;

    @Column(name = "upi_id")
    private String upiId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.INITIATED;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 1;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "initiated_at")
    private LocalDateTime initiatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "is_reversed")
    private boolean isReversed = false;

    @Column(name = "reversed_at")
    private LocalDateTime reversedAt;

    @PrePersist void onCreate() { initiatedAt = LocalDateTime.now(); }

    public enum PaymentType   { PREMIUM, PAYOUT }
    public enum Direction     { INBOUND, OUTBOUND }
    public enum EntityType    { POLICY, CLAIM }
    public enum Channel       { UPI, IMPS, RAZORPAY, STRIPE }
    public enum PaymentStatus { INITIATED, PROCESSING, SUCCESS, FAILED, REVERSED }

    // ── Constructors ─────────────────────────────────────────
    public Payment() {}

    private Payment(Builder b) {
        this.referenceId = b.referenceId; this.type = b.type; this.direction = b.direction;
        this.entityType = b.entityType; this.entityId = b.entityId; this.user = b.user;
        this.amount = b.amount; this.currency = b.currency; this.channel = b.channel;
        this.upiId = b.upiId; this.status = b.status; this.attemptCount = b.attemptCount;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String referenceId, upiId, channelRef, failureReason;
        private String currency = "INR";
        private PaymentType type;
        private Direction direction;
        private EntityType entityType;
        private Long entityId;
        private User user;
        private Integer amount;
        private Channel channel;
        private PaymentStatus status = PaymentStatus.INITIATED;
        private Integer attemptCount = 1;

        public Builder referenceId(String v)        { this.referenceId = v; return this; }
        public Builder type(PaymentType v)          { this.type = v; return this; }
        public Builder direction(Direction v)       { this.direction = v; return this; }
        public Builder entityType(EntityType v)     { this.entityType = v; return this; }
        public Builder entityId(Long v)             { this.entityId = v; return this; }
        public Builder user(User v)                 { this.user = v; return this; }
        public Builder amount(Integer v)            { this.amount = v; return this; }
        public Builder currency(String v)           { this.currency = v; return this; }
        public Builder channel(Channel v)           { this.channel = v; return this; }
        public Builder upiId(String v)              { this.upiId = v; return this; }
        public Builder status(PaymentStatus v)      { this.status = v; return this; }
        public Builder attemptCount(Integer v)      { this.attemptCount = v; return this; }
        public Payment build()                      { return new Payment(this); }
    }

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                         { return id; }
    public String getReferenceId()              { return referenceId; }
    public void setReferenceId(String v)        { this.referenceId = v; }
    public PaymentType getType()                { return type; }
    public void setType(PaymentType v)          { this.type = v; }
    public Direction getDirection()             { return direction; }
    public EntityType getEntityType()           { return entityType; }
    public Long getEntityId()                   { return entityId; }
    public User getUser()                       { return user; }
    public Integer getAmount()                  { return amount; }
    public void setAmount(Integer v)            { this.amount = v; }
    public String getCurrency()                 { return currency; }
    public Channel getChannel()                 { return channel; }
    public String getChannelRef()               { return channelRef; }
    public void setChannelRef(String v)         { this.channelRef = v; }
    public String getUpiId()                    { return upiId; }
    public PaymentStatus getStatus()            { return status; }
    public void setStatus(PaymentStatus v)      { this.status = v; }
    public Integer getAttemptCount()            { return attemptCount; }
    public void setAttemptCount(Integer v)      { this.attemptCount = v; }
    public String getFailureReason()            { return failureReason; }
    public void setFailureReason(String v)      { this.failureReason = v; }
    public LocalDateTime getInitiatedAt()       { return initiatedAt; }
    public LocalDateTime getCompletedAt()       { return completedAt; }
    public void setCompletedAt(LocalDateTime v) { this.completedAt = v; }
    public boolean isReversed()                 { return isReversed; }
    public void setReversed(boolean v)          { this.isReversed = v; }
    public LocalDateTime getReversedAt()        { return reversedAt; }
    public void setReversedAt(LocalDateTime v)  { this.reversedAt = v; }
}
