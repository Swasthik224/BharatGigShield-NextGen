package com.paramins.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "log_date"}))
public class ActivityLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "active_hours", precision = 4, scale = 2)
    private BigDecimal activeHours;

    private Integer deliveries;

    @Column(name = "platform_login")
    private Boolean platformLogin = false;

    @Column(name = "gps_city")
    private String gpsCity;

    @Column(name = "gps_ward")
    private String gpsWard;

    @Column(name = "gps_lat", precision = 10, scale = 7)
    private BigDecimal gpsLat;

    @Column(name = "gps_lng", precision = 10, scale = 7)
    private BigDecimal gpsLng;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    // ── Constructors ─────────────────────────────────────────
    public ActivityLog() {}

    private ActivityLog(Builder b) {
        this.user = b.user; this.logDate = b.logDate; this.activeHours = b.activeHours;
        this.deliveries = b.deliveries; this.platformLogin = b.platformLogin;
        this.gpsCity = b.gpsCity; this.gpsWard = b.gpsWard;
        this.gpsLat = b.gpsLat; this.gpsLng = b.gpsLng;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User user;
        private LocalDate logDate;
        private BigDecimal activeHours, gpsLat, gpsLng;
        private Integer deliveries;
        private Boolean platformLogin = false;
        private String gpsCity, gpsWard;

        public Builder user(User v)              { this.user = v; return this; }
        public Builder logDate(LocalDate v)      { this.logDate = v; return this; }
        public Builder activeHours(BigDecimal v) { this.activeHours = v; return this; }
        public Builder deliveries(Integer v)     { this.deliveries = v; return this; }
        public Builder platformLogin(Boolean v)  { this.platformLogin = v; return this; }
        public Builder gpsCity(String v)         { this.gpsCity = v; return this; }
        public Builder gpsWard(String v)         { this.gpsWard = v; return this; }
        public Builder gpsLat(BigDecimal v)      { this.gpsLat = v; return this; }
        public Builder gpsLng(BigDecimal v)      { this.gpsLng = v; return this; }
        public ActivityLog build()               { return new ActivityLog(this); }
    }

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                         { return id; }
    public User getUser()                       { return user; }
    public LocalDate getLogDate()               { return logDate; }
    public BigDecimal getActiveHours()          { return activeHours; }
    public Integer getDeliveries()              { return deliveries; }
    public Boolean getPlatformLogin()           { return platformLogin; }
    public void setPlatformLogin(Boolean v)     { this.platformLogin = v; }
    public String getGpsCity()                  { return gpsCity; }
    public void setGpsCity(String v)            { this.gpsCity = v; }
    public String getGpsWard()                  { return gpsWard; }
    public BigDecimal getGpsLat()               { return gpsLat; }
    public BigDecimal getGpsLng()               { return gpsLng; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
}
