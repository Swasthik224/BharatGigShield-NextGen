package com.paramins.dto;

public class AuthResponse {

    private String token;
    private boolean isNew;
    private String phone;
    private String role; // ✅ role field

    public AuthResponse() {}

    // ✅ Main constructor
    public AuthResponse(String token, boolean isNew, String phone, String role) {
        this.token = token;
        this.isNew = isNew;
        this.phone = phone;
        this.role = role;
    }

    // 🔹 Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}