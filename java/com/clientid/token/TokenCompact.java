package com.clientid.token;

import java.security.NoSuchAlgorithmException;

public interface TokenCompact {
	public Token compact() throws NoSuchAlgorithmException;
}
