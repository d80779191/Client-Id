package com.clientid.initial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JobProperties implements IInitialized {
	private final String FONFIG_NAME = "/res/config.properties";
	private Properties properties = new Properties();
	
	@Override
	public String exec() {
		properties.clear();		
		StringBuilder sb = new StringBuilder();
		sb.append(FONFIG_NAME);
	    InputStream  inputStream = getClass().getClassLoader().getResourceAsStream(FONFIG_NAME);
	    if (inputStream != null) {
	        try {
	        		properties.load(inputStream);
	        		sb.append(String.format("%s : %d", FONFIG_NAME, properties.size()));
	        		for(String key : properties.stringPropertyNames()) {
	        			  String value = properties.getProperty(key);
	        			  sb.append(String.format("%s = %s", key, value));
	        		}
	        } catch (IOException e) {
	        		sb.append(String.format("%s : %s", FONFIG_NAME, e.getMessage()));
	        }
	    } else {
	    		sb.append("config.properties : Not Found");
	    }
	    return sb.toString();
	}
	
	public Properties getProperties() {
		return this.properties;
	}
}
