package com.paramins.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(nullable = false, length = 100)
    private String name;

    private String email;

    @Column(nullable = false, length = 60)
    private String city;

    @Column(name = "upi_id", nullable = false, length = 100)
    private String upiId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(name = "platform_worker_id", length = 60)
    private String platformWorkerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkerTier tier = WorkerTier.TIER_C;

    // ✅ NEW ROLE FIELD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "kyc_verified", nullable = false)
    private boolean kycVerified = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public enum Platform { ZOMATO, SWIGGY, ZEPTO, OTHER }
    public enum WorkerTier { TIER_A, TIER_B, TIER_C }

    // ✅ NEW ROLE ENUM
    public enum Role { USER, ADMIN }

    // ── Constructors ─────────────────────────
    public User() {}

    private User(Builder b) {
        this.phone = b.phone;
        this.name = b.name;
        this.email = b.email;
        this.city = b.city;
        this.upiId = b.upiId;
        this.platform = b.platform;
        this.platformWorkerId = b.platformWorkerId;
        this.tier = b.tier;
        this.role = b.role; // ✅ added
        this.isActive = b.isActive;
        this.kycVerified = b.kycVerified;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String phone, name, email, city, upiId, platformWorkerId;
        private Platform platform;
        private WorkerTier tier = WorkerTier.TIER_C;
        private Role role = Role.USER; // ✅ added
        private boolean isActive = true, kycVerified = false;

        public Builder phone(String v) { this.phone = v; return this; }
        public Builder name(String v) { this.name = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder city(String v) { this.city = v; return this; }
        public Builder upiId(String v) { this.upiId = v; return this; }
        public Builder platform(Platform v) { this.platform = v; return this; }
        public Builder platformWorkerId(String v) { this.platformWorkerId = v; return this; }
        public Builder tier(WorkerTier v) { this.tier = v; return this; }
        public Builder role(Role v) { this.role = v; return this; } // ✅ added
        public Builder isActive(boolean v) { this.isActive = v; return this; }
        public Builder kycVerified(boolean v) { this.kycVerified = v; return this; }

        public User build() { return new User(this); }
    }

    // ── Getters & Setters ────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }

    public String getName() { return name; }
    public void setName(String v) { this.name = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getCity() { return city; }
    public void setCity(String v) { this.city = v; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String v) { this.upiId = v; }

    public Platform getPlatform() { return platform; }
    public void setPlatform(Platform v) { this.platform = v; }

    public String getPlatformWorkerId() { return platformWorkerId; }
    public void setPlatformWorkerId(String v) { this.platformWorkerId = v; }

    public WorkerTier getTier() { return tier; }
    public void setTier(WorkerTier v) { this.tier = v; }

    // ✅ ROLE getter/setter
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean v) { this.isActive = v; }

    public boolean isKycVerified() { return kycVerified; }
    public void setKycVerified(boolean v) { this.kycVerified = v; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}