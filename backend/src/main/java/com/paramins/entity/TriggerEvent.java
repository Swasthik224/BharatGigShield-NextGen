package com.paramins.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trigger_events",
    uniqueConstraints = @UniqueConstraint(columnNames = {"city", "risk_type", "event_date"}))
public class TriggerEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String city;

    private String ward;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type", nullable = false)
    private Policy.RiskType riskType;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "observed_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal observedValue;

    @Column(name = "threshold_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal thresholdValue;

    @Column(name = "data_source", nullable = false, length = 50)
    private String dataSource;

    @Column(name = "is_validated")
    private boolean isValidated = false;

    @Column(name = "validation_note")
    private String validationNote;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    // ── Constructors ─────────────────────────────────────────
    public TriggerEvent() {}

    private TriggerEvent(Builder b) {
        this.city = b.city; this.ward = b.ward; this.riskType = b.riskType;
        this.eventDate = b.eventDate; this.observedValue = b.observedValue;
        this.thresholdValue = b.thresholdValue; this.dataSource = b.dataSource;
        this.isValidated = b.isValidated; this.validationNote = b.validationNote;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String city, ward, dataSource, validationNote;
        private Policy.RiskType riskType;
        private LocalDate eventDate;
        private BigDecimal observedValue, thresholdValue;
        private boolean isValidated = false;

        public Builder city(String v)               { this.city = v; return this; }
        public Builder ward(String v)               { this.ward = v; return this; }
        public Builder riskType(Policy.RiskType v)  { this.riskType = v; return this; }
        public Builder eventDate(LocalDate v)       { this.eventDate = v; return this; }
        public Builder observedValue(BigDecimal v)  { this.observedValue = v; return this; }
        public Builder thresholdValue(BigDecimal v) { this.thresholdValue = v; return this; }
        public Builder dataSource(String v)         { this.dataSource = v; return this; }
        public Builder isValidated(boolean v)       { this.isValidated = v; return this; }
        public Builder validationNote(String v)     { this.validationNote = v; return this; }
        public TriggerEvent build()                 { return new TriggerEvent(this); }
    }

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                         { return id; }
    public void setId(Long v)                   { this.id = v; }
    public String getCity()                     { return city; }
    public void setCity(String v)               { this.city = v; }
    public String getWard()                     { return ward; }
    public void setWard(String v)               { this.ward = v; }
    public Policy.RiskType getRiskType()        { return riskType; }
    public void setRiskType(Policy.RiskType v)  { this.riskType = v; }
    public LocalDate getEventDate()             { return eventDate; }
    public void setEventDate(LocalDate v)       { this.eventDate = v; }
    public BigDecimal getObservedValue()        { return observedValue; }
    public void setObservedValue(BigDecimal v)  { this.observedValue = v; }
    public BigDecimal getThresholdValue()       { return thresholdValue; }
    public void setThresholdValue(BigDecimal v) { this.thresholdValue = v; }
    public String getDataSource()               { return dataSource; }
    public void setDataSource(String v)         { this.dataSource = v; }
    public boolean isValidated()                { return isValidated; }
    public void setValidated(boolean v)         { this.isValidated = v; }
    public String getValidationNote()           { return validationNote; }
    public void setValidationNote(String v)     { this.validationNote = v; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
}
