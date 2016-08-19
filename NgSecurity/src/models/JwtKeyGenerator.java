package models;

import java.security.Key;

import javax.crypto.KeyGenerator;

public class JwtKeyGenerator {
	public static Key generate() {
		Key k = null;
		try {
			k = KeyGenerator.getInstance("AES").generateKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return k;
	}
}
