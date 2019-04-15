package com.clientid.dto;

public class ClientIDListDTO {
	private String hashKey;
	private String hashValue;
	private String secretKey;
	private String role;
	private String clientId;

	public ClientIDListDTO() {
	
	}
	public ClientIDListDTO(String hashKey, String hashValue, String secretKey, String role, String clientId/*, String hashKey2*/) {
		this.hashKey = hashKey;
		this.hashValue = hashValue;
		this.secretKey = secretKey;
		this.role = role;
		this.clientId = clientId;
	}
	public String getHashKey() {
		return hashKey;
	}
	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
