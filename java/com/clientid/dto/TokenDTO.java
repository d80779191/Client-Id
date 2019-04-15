package com.clientid.dto;

public class TokenDTO {
	private String token;
	
	public TokenDTO(String token) {
		this.token = token;
	}
	public String getToken() {
		return this.token;
	}

	public void setToken(String value) {
		this.token = value;
	}

}
