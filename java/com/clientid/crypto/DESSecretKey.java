package com.clientid.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESSecretKey {
	static final String ALG = "DES";
	public static final String ISSUER = "Allen.Lin"; // load form resource file

	
	public static SecretKey generateKey() throws NoSuchAlgorithmException 
	{
		return generateKey(DESSecretKey.ISSUER);
	}
	private static SecretKey generateKey(String encryptKey) throws NoSuchAlgorithmException 
	{
		KeyGenerator keyGen = KeyGenerator.getInstance(ALG);
		keyGen.init(new SecureRandom((encryptKey).getBytes()));
		return keyGen.generateKey();
	}
	
	public static String getKeyString(SecretKey key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	public static SecretKey getKey(String base64Key) {
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALG);
	}	
}
