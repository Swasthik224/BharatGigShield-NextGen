package com.paramins.dto;

import com.paramins.entity.User;

public class UserDto {
    private Long id;
    private String name, phone, city, upiId, tier, platform;

    public UserDto() {}

    private UserDto(Builder b) {
        this.id = b.id; this.name = b.name; this.phone = b.phone;
        this.city = b.city; this.upiId = b.upiId; this.tier = b.tier; this.platform = b.platform;
    }

    public static UserDto from(User u) {
        return new Builder().id(u.getId()).name(u.getName()).phone(u.getPhone())
            .city(u.getCity()).upiId(u.getUpiId())
            .tier(u.getTier().name()).platform(u.getPlatform().name()).build();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name, phone, city, upiId, tier, platform;

        public Builder id(Long v)           { this.id = v; return this; }
        public Builder name(String v)       { this.name = v; return this; }
        public Builder phone(String v)      { this.phone = v; return this; }
        public Builder city(String v)       { this.city = v; return this; }
        public Builder upiId(String v)      { this.upiId = v; return this; }
        public Builder tier(String v)       { this.tier = v; return this; }
        public Builder platform(String v)   { this.platform = v; return this; }
        public UserDto build()              { return new UserDto(this); }
    }

    public Long getId()         { return id; }
    public String getName()     { return name; }
    public String getPhone()    { return phone; }
    public String getCity()     { return city; }
    public String getUpiId()    { return upiId; }
    public String getTier()     { return tier; }
    public String getPlatform() { return platform; }
}
