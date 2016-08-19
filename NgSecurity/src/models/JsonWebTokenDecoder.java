package models;

import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import data.JwsKeyDao;
import data.UserDao;
import entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolver;

@Component
public class JsonWebTokenDecoder {
	@Autowired
	JwsKeyDao keyDao;
	
	@Autowired
	private UserDao userDao;
	
	public User decodeUserJwt(String signedJwt) {
		JsonWebTokenDecoder decoder = new JsonWebTokenDecoder();
		Jws<Claims> jws = Jwts.parser().setSigningKeyResolver(new SigningKeyResolver() {

			@Override
			public Key resolveSigningKey(JwsHeader header, Claims claims) {
				if (header.containsKey("kid")) {
					long id = (int) header.get("kid");
					Key k = keyDao.show(id);
					return k;
				}
				return null;
			}

			@Override
			public Key resolveSigningKey(JwsHeader header, String claims) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}).parseClaimsJws(signedJwt); 
		
		long id;
		
		try {
			// Note...this needs to be cast to an int and not a long for unknown reason
			 id = (int) jws.getBody().get("user_id");
		} catch (Exception e) {
			return null;
		}
		return userDao.show(id);
	}
}
