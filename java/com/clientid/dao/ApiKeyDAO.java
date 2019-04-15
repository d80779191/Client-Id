package com.clientid.dao;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.clientid.initial.JedisListener;
import com.clientid.initial.JobRedis;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

public class ApiKeyDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String hashKey;
	private String hashValue;
	private String secretKey;
	private String role;
	private String clientId;

	public ApiKeyDAO() {
	
	}
	public ApiKeyDAO(String hashKey, String hashValue, String secretKey, String role, String clientId/*, String hashKey2*/) {
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
	
	public static ApiKeyDAO findByHashKey(String key) {
		JedisGetListener listener = new JedisGetListener(key);
		JobRedis.getJedis(listener);
		String jsonData = listener.message;
		if (StringUtils.isAnyBlank(jsonData)){
			return null;
		}
		return new Gson().fromJson(jsonData, ApiKeyDAO.class);
	}
	public static void refresh(final String key, final int seconds) {
		ApiKeyDAO dao = findByHashKey(key);
		if (null != dao) {
			_refresh(dao.getHashKey(), seconds);
		}
	}
	private static void _refresh(final String key, final int seconds) {
		JobRedis.getJedis(new JedisListener() {
			@Override
			public void ready(Jedis jedis) {
				long state = jedis.expire(key, seconds);
				System.out.println(String.format("jedis_expire():",state,key));
			}
		});	
	}
	public static void deleteByHashKey(String key) {
		ApiKeyDAO dao = findByHashKey(key);
		if (null != dao) {
			_deleteByHashKey(dao.getHashKey());
		}
	}
	public static void deleteByOwner(String id) {
		JobRedis.getJedis(new JedisListener() {
			@Override
			public void ready(Jedis jedis) {
				String idxStr = "sub_"+id+";*";
				Set<String> keys = jedis.keys(idxStr);
				System.out.println(String.format("deleteByOwner(%s):%s",keys.size(), idxStr));
				for (String key : keys) {
					deleteByHashKey(key);	
				}
			}
		});	
	}
	
	private static void _deleteByHashKey(String key) {
		JobRedis.getJedis(new JedisListener() {
			@Override
			public void ready(Jedis jedis) {
				long cnt = jedis.del(key);
				System.out.println(String.format("jedis_key(%s):", cnt, key));
			}
		});				
	}
	public boolean save() {
		return save(60);
	}
	public boolean save(final int seconds) {
		String key1 = this.getHashKey();  // 蝝Ｗ��嚗迤��揣撘�)
		String value = new Gson().toJson(this);
		JedisSetListener listener = new JedisSetListener(key1/*, key2*/, value, seconds);
		JobRedis.getJedis(listener);
		return "OK".equals(listener.message);
	}
	private static class JedisGetListener implements JedisListener{
		String key = "";
		String message = "";
		
		JedisGetListener(String key){
			this.key = key;
		}
		@Override
		public void ready(Jedis jedis) {
			message = jedis.get(key);
		}
	}
	private static class JedisSetListener implements JedisListener{
		String key1 = "";
		String value = "";
		int seconds = 0;
		String message = "";
		
		JedisSetListener(String key1, String value, int seconds){
			this.key1 = key1;
			this.value = value;
			this.seconds = seconds;
		}
		@Override
		public void ready(Jedis jedis) {
				if (seconds > 0) {
					message = jedis.setex(key1, seconds, value);
				} else {
					message = jedis.set(key1, value);	
				}
			System.out.println("jedis_key1:" + key1);
		}
	}	
}
