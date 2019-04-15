package com.clientid.initial;

import redis.clients.jedis.Jedis;

public interface JedisListener {
	public void ready(Jedis jedis);
}
