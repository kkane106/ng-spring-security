package models;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;

import org.springframework.beans.factory.annotation.Autowired;

import data.JwsKeyDao;
import entities.JwsKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JsonWebTokenGenerator {

	public String generateUserJwt(long userId, Key key, long keyId) {
		Date now = new Date();
		Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
		
		System.out.println("GOT HERE");
		Map<String, Object> userJson = new HashMap<>();
		userJson.put("user_id", userId);
		
		String jwt = "";
		try {
			jwt = Jwts.builder().setHeaderParam("kid", keyId).setSubject("user").setClaims(userJson).setExpiration(tomorrow)
					.signWith(SignatureAlgorithm.HS256, key).compact();
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		return jwt;
	}
	
}
