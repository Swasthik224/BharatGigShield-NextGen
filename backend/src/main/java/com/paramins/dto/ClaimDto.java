package com.paramins.dto;

import com.paramins.entity.Claim;
import java.time.LocalDate;

public class ClaimDto {
    private Long id;
    private String claimNumber, status, riskType;
    private double payoutAmountRupees;
    private LocalDate claimDate;

    public ClaimDto() {}

    private ClaimDto(Builder b) {
        this.id = b.id; this.claimNumber = b.claimNumber; this.status = b.status;
        this.riskType = b.riskType; this.payoutAmountRupees = b.payoutAmountRupees;
        this.claimDate = b.claimDate;
    }

    public static ClaimDto from(Claim c) {
        return new Builder()
            .id(c.getId()).claimNumber(c.getClaimNumber()).status(c.getStatus().name())
            .payoutAmountRupees(c.getPayoutAmount() / 100.0).claimDate(c.getClaimDate())
            .riskType(c.getTriggerEvent() != null ? c.getTriggerEvent().getRiskType().name() : "")
            .build();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String claimNumber, status, riskType;
        private double payoutAmountRupees;
        private LocalDate claimDate;

        public Builder id(Long v)                       { this.id = v; return this; }
        public Builder claimNumber(String v)            { this.claimNumber = v; return this; }
        public Builder status(String v)                 { this.status = v; return this; }
        public Builder riskType(String v)               { this.riskType = v; return this; }
        public Builder payoutAmountRupees(double v)     { this.payoutAmountRupees = v; return this; }
        public Builder claimDate(LocalDate v)           { this.claimDate = v; return this; }
        public ClaimDto build()                         { return new ClaimDto(this); }
    }

    public Long getId()                     { return id; }
    public String getClaimNumber()          { return claimNumber; }
    public String getStatus()               { return status; }
    public String getRiskType()             { return riskType; }
    public double getPayoutAmountRupees()   { return payoutAmountRupees; }
    public LocalDate getClaimDate()         { return claimDate; }
}
