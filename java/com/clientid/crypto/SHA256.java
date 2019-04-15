package com.clientid.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

public class SHA256 {
	public static String digest(final String content) throws NoSuchAlgorithmException  
	  {  
	    String result = null;  
	    if (!StringUtils.isAnyBlank(content))
	    {  
	        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");  
	        sha256.update(content.getBytes());  
	        byte[] bytes = sha256.digest();
	        StringBuilder sbHex = new StringBuilder();
	        for (int i = 0; i < bytes.length; i++)  
	        {  
	          String hex = Integer.toHexString(0xff & bytes[i]);  
	          if (hex.length() == 1) 
	          {  
	        	  	sbHex.append('0');  
	          }  
	          	sbHex.append(hex);  
	        }  
	        result = sbHex.toString();  
	        
	      }  
	    return result;  
	  }
}
