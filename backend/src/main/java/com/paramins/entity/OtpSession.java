package com.paramins.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_sessions")
public class OtpSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(name = "otp_hash", nullable = false, length = 100)
    private String otpHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    private boolean used = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    // ── Constructors ─────────────────────────────────────────
    public OtpSession() {}

    private OtpSession(Builder b) {
        this.phone = b.phone; this.otpHash = b.otpHash; this.expiresAt = b.expiresAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String phone, otpHash;
        private LocalDateTime expiresAt;

        public Builder phone(String v)          { this.phone = v; return this; }
        public Builder otpHash(String v)        { this.otpHash = v; return this; }
        public Builder expiresAt(LocalDateTime v){ this.expiresAt = v; return this; }
        public OtpSession build()               { return new OtpSession(this); }
    }

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                         { return id; }
    public String getPhone()                    { return phone; }
    public String getOtpHash()                  { return otpHash; }
    public LocalDateTime getExpiresAt()         { return expiresAt; }
    public boolean isUsed()                     { return used; }
    public void setUsed(boolean v)              { this.used = v; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
}
