package com.paramins.dto;
public class RegisterRequest {
    private String phone, name, city, upiId, platform;
    public String getPhone()            { return phone; }
    public void setPhone(String v)      { this.phone = v; }
    public String getName()             { return name; }
    public void setName(String v)       { this.name = v; }
    public String getCity()             { return city; }
    public void setCity(String v)       { this.city = v; }
    public String getUpiId()            { return upiId; }
    public void setUpiId(String v)      { this.upiId = v; }
    public String getPlatform()         { return platform; }
    public void setPlatform(String v)   { this.platform = v; }
}
