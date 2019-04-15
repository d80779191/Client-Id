package com.clientid.initial;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JobRedis implements IInitialized {
	
	private static JedisPool pool = null; // new JedisPool(new JedisPoolConfig(), JobRedis.HostString);
	public static String HostString = null;
	public static String PWD = null;
	private Properties properties;
	private final String HOST = "localhost"; 
	public JobRedis(String host) {
		this(host, null);
	}
	
	public JobRedis(Properties properties) {
		this(null, properties);
	}

	public JobRedis(String host, Properties properties) {
		this.properties = properties;
		JobRedis.HostString = this.properties.getProperty("Jedis", host);
		JobRedis.PWD = this.properties.getProperty("JedisPwd");
		//System.out.println("Jedis : " + JobRedis.HostString);
		if (null == JobRedis.HostString) {
			JobRedis.HostString = HOST;
		}
		System.out.println("Jedis : " + JobRedis.HostString);
		System.out.println("Jedis : " + JobRedis.PWD);
	}

	@Override
	public String exec() {
		StringBuilder sb = new StringBuilder();
		try 
		{
			JobRedis.getJedis(new JedisListener() {
				@Override
				public void ready(Jedis jedis) {
					String status = jedis.setex(JobRedis.class.getName(), 180, "test by gordon");
					sb.append(String.format("Redis Testing: %s", status));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			sb.append(String.format("Redis Error: %s", e.getMessage()));
		}
		return sb.toString();
	}
	
	public static void getJedis(JedisListener listener)
	{
		if (null == listener) {
			return;
		}
		Jedis jedis = null;
		try {
			if (null == pool) {
				if (StringUtils.isAnyBlank(JobRedis.PWD)) {
					JobRedis.pool = new JedisPool(new JedisPoolConfig(), JobRedis.HostString);
				} else {
				//	System.out.println("pwd:" + JobRedis.PWD);
					JobRedis.pool = new JedisPool(new JedisPoolConfig(), JobRedis.HostString, 6379, 2000, JobRedis.PWD);	
				}
			}
		    jedis = pool.getResource();
		    //if (!StringUtils.isAnyBlank(JobRedis.PWD)) {
		    		//jedis.auth(JobRedis.PWD);
		    //}
		    listener.ready(jedis);
		} catch (Exception e) {
			System.out.println("Jedis : " + JobRedis.HostString);
			System.out.println("Jedis : " + JobRedis.PWD);
			e.printStackTrace();
		} finally {
		    if (jedis != null) {
		        jedis.close();
		    }
		}
	}
	
	public static void close() {
		if (null != pool ) {
			pool.destroy();
			pool = null;
		}
	}
	
}
