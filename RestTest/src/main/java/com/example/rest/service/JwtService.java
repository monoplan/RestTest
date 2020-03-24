package com.example.rest.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.rest.exception.UnauthorizedException;

@Service("jwtService")
public class JwtService {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String SALT =  "secureKey";

//	public <T> String create(String key, T data, String subject){
	public <T> String makeToken(Map map){
	    
	    Map<String, Object> headers = new HashMap<>();    
	    headers.put("typ", "JWT");
	    headers.put("alg", "HS256");
	     
	    Map<String, Object> payloads = new HashMap<>();
	    Long expiredTime = 1000 * 60l ; // 만료기간 1분    
	    Date now = new Date();
	    now.setTime(now.getTime() + expiredTime);    
	    payloads.put("exp", now);
	    payloads.put("data", map);
	    
	    String jwt = Jwts.builder()
	            .setHeader(headers)
	            .setClaims(payloads)
	            .signWith(SignatureAlgorithm.HS256, this.getKeyByte())
	            .compact();
	    
	    return jwt;   
		
	}	

	public byte[] getKeyByte(){
		byte[] key = null;
		try {
			key = SALT.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			if(log.isInfoEnabled()){
				e.printStackTrace();
			}else{
				log.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}
		
		return key;
	}
	
	public boolean checkToken(String jwt) {
		Jws<Claims> claims = null ; 
		try{
			claims = Jwts.parser()
					  .setSigningKey(this.getKeyByte())
					  .parseClaimsJws(jwt);
			return true;
			
		}catch (Exception e) {
			throw new UnauthorizedException();
		}
	}	
	
	public void getDataFromToken(String jwtTokenString) throws InterruptedException {
/*
		Claims claims = Jwts.parser()
	        .setSigningKey(this.getKeyByte())
	        .parseClaimsJws(jwtTokenString)
	        .getBody();
	    
		log.info("■ 복원된 데이타 : "+ value.toString());
	    
	    Date expiration = claims.get("exp", Date.class);
        log.info("■ exp:"+ expiration.toString());

        Map data = claims.get("data", Map.class);
        log.info("■ data:"+ data.toString());
*/

		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
						 .setSigningKey(SALT.getBytes("UTF-8"))
						 .parseClaimsJws(jwtTokenString);
		} catch (Exception e) {
			throw new UnauthorizedException();
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> value = (HashMap<String, Object>)claims.getBody().get("data");
		log.info("■복원된 데이터:"+ value.toString() );
	}
	
}
