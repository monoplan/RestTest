package com.example.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.rest.exception.UnauthorizedException;
import com.example.rest.service.JwtService;

@Component
public class JwtInterceptor implements HandlerInterceptor {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String HEADER_AUTH = "Authorization";
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		final String token = request.getHeader(HEADER_AUTH);

		if(token != null && jwtService.checkToken(token)){
			
			jwtService.getDataFromToken(token) ; 
			
			return true;
		}else{
			throw new UnauthorizedException();
		}
	}	

}
