package com.clientid.token;

import java.util.Date;

import com.google.gson.JsonObject;

public abstract class TokenBase {
	private String issue;
	private Date issuedAt;
	private Date expiration;
	private String role;
	private String owner;
	private String hashKey;
	private String hashKey2;
	private String hashValue;
	private String key; // private key
	private JsonObject user = null;
	public TokenBase(String role, String owner) {
		this(role, owner, "");
	}
	public TokenBase(String role, String owner, String issue) {
		this(role, owner, issue, null);
	}
	public TokenBase(String role, String owner, String issue, JsonObject user) {
		this.setRole(role);
		this.setOwner(owner);
		this.setUser(user);
		this.setIssue(issue);
		this.issue = "Allen.Lin";
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String value) {
		this.issue = value;
	}
	public Date getIssuedAt() {
		return issuedAt;
	}
	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}
	public Date getExpiration() {
		return expiration;
	}
	public JsonObject getUser() {
		return user;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public void setUser(JsonObject usr) {
		this.user = usr;
	}
	public String getHashKey2() {
		return hashKey2;
	}
	public void setHashKey2(String hashKey2) {
		this.hashKey2 = hashKey2;
	}
	
}
