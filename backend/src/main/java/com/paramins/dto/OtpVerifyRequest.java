package com.paramins.dto;

public class OtpVerifyRequest {
	private String phone, otp;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String v) {
		this.phone = v;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String v) {
		this.otp = v;
	}
}
