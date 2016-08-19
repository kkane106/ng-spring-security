package controllers;

import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import data.JwsKeyDao;
import data.UserDao;
import entities.User;
import models.JsonWebTokenGenerator;
import models.JwtKeyGenerator;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	JwsKeyDao keyDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody String userJsonString) {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(userJsonString);
		User user = null;
		try {
			user = mapper.readValue(userJsonString, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			user = userDao.authenticateUser(user);
		} catch (NoResultException e) {
			Map<String,String> errJson = new HashMap<>();
			errJson.put("message", "Username not found");
			return new ResponseEntity<Map<String,String>>(errJson, HttpStatus.NOT_FOUND);
		}
		
		if (user != null) {
			Key key = JwtKeyGenerator.generate();
			long keyId = keyDao.create(key).getId();
			JsonWebTokenGenerator jwtGen = new JsonWebTokenGenerator();
			String jws = jwtGen.generateUserJwt(user.getId(), key, keyId);
			Map<String,String> success = new HashMap<>();
			success.put("jwt", jws);
			return new ResponseEntity<Map<String,String>>(success, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Incorrect password", HttpStatus.UNAUTHORIZED);
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> signup(@RequestBody String userJsonString){
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(userJsonString, User.class);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			try {
				user = userDao.create(user);
			} catch (PersistenceException pe) {
				Map<String,String> usernameUnavailable = new HashMap<>();
				usernameUnavailable.put("error", "Chosen username is not available");
				return new ResponseEntity<Map<String,String>>(usernameUnavailable, HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} catch (JsonParseException jpe) {
			jpe.printStackTrace();
		} catch (JsonMappingException jme) {
			jme.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		
		if (user != null) {
			Key key = JwtKeyGenerator.generate();
			long keyId = keyDao.create(key).getId();
			JsonWebTokenGenerator jwtGen = new JsonWebTokenGenerator();
			String jws = jwtGen.generateUserJwt(user.getId(), key, keyId);
			Map<String,String> success = new HashMap<>();
			success.put("jwt", jws);
			return new ResponseEntity<Map<String,String>>(success, HttpStatus.OK);
		}
		Map<String,String> errJson = new HashMap<>();
		errJson.put("error", "Something went wrong");
		return new ResponseEntity<Map<String,String>>(errJson, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "/access-denied")
	public ResponseEntity<?> denied() {
		return new ResponseEntity<String>("Access Denied", HttpStatus.UNAUTHORIZED);
	}
}
