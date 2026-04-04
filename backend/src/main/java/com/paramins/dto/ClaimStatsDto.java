package com.paramins.dto;
public class ClaimStatsDto {
    private long total, paid, pending, rejected;
    public ClaimStatsDto() {}
    public ClaimStatsDto(long total, long paid, long pending, long rejected) {
        this.total = total; this.paid = paid; this.pending = pending; this.rejected = rejected;
    }
    public long getTotal()          { return total; }
    public long getPaid()           { return paid; }
    public long getPending()        { return pending; }
    public long getRejected()       { return rejected; }
}
