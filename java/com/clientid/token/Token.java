package com.clientid.token;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.clientid.crypto.DESSecretKey;
import com.clientid.crypto.SHA256;
import com.google.gson.JsonObject;

public class Token extends TokenBase {
	
	public Token(String role, String owner) {
		this(role, owner, null);
	}
	public Token(String role, String owner, JsonObject user) {
		this(role, owner, "", user);
	}
	public Token(String role, String owner, String issue, JsonObject user) {
		super(role, owner, issue, user);
	}
	public static String compact(String context) {
		return null == context ? DESSecretKey.ISSUER : context + DESSecretKey.ISSUER;
	}
	public static String digest(String context) throws NoSuchAlgorithmException {
		return SHA256.digest(Token.compact(context));
	}
	public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
/*
 * public abstract class Token {
	static final String ISSUER = "Gordon.C.Cheng";
	private String token;
	private String owner;
	private SecretKey key;
	public Token(String owner) {
		this.owner = owner;
	}
	public String getOwner() {
		return this.owner;
	}
	public String generate() throws NoSuchAlgorithmException {
		// Step1. ���遣蝡���� (敹��葉摮���瘜�圾)
		this.key = generateKey();
		// Step2. ���誘���(�撖阡�隞嗅遣蝡�)
		this.token = createToken();
		return this.token;
	}
	public SecretKey getKey() throws NoSuchAlgorithmException {
		if (null == this.key) {
			throw new NoSuchAlgorithmException();
		}
		return this.key;
	}
	public String getKeyString() throws NoSuchAlgorithmException {
		// ���頧�ase64摮葡�撘�,��靘摮�
		return Base64.getEncoder().encodeToString(getKey().getEncoded());
	}
	public SecretKey generateKey() throws NoSuchAlgorithmException 
	{
		byte[] key = (this.getClass().getName() + ISSUER).getBytes();
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(new SecureRandom(key));
		return keyGen.generateKey();
	}
	public static String findByToken(String token) {
		return WebApplication.jedis.get(token);
	}
	public static void deleteByToken(String token) {
		WebApplication.jedis.del(token);
	}
	public abstract String createToken() throws NoSuchAlgorithmException;
	public abstract void getKeyValue();
	public void save(String key, String value) {
		int seconds = 24 * 60 * 60;
		String status = WebApplication.jedis.setex(key, seconds, value);
		if (!"OK".equals(status)) {
			System.out.println(status);
			throw new JedisDataException("setex:"+status);
		}
	}
}

 */
