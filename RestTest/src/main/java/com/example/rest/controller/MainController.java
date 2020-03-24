package com.example.rest.controller;


import java.util.HashMap;
import java.util.Map;


import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.service.JwtService;
import com.example.rest.util.JsonUtils;

@RestController
public class MainController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JwtService jwtService;
	
	@RequestMapping("/hello")
	@ResponseBody
	public JSONObject hello(@RequestBody JSONObject obj) {
		
		log.info("■받은값...:"+ obj);
		
		String val = obj.get("val").toString() ; 
		
		JSONObject res = new JSONObject();
		res.put("result", "ok");
		res.put("val", val);
		return res;
	}
	
    @RequestMapping(value="/login")
    public ResponseEntity<?> signin(@RequestBody JSONObject json){
		log.info("■받은값...:"+ json);

		Map<String, Object> map = JsonUtils.getMapFromJsonObject(json);
		String jwt = jwtService.makeToken(map) ;  

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", jwt) ; 
		/*
		JSONObject obj = new JSONObject();
		obj.put("result", jwt);
		return obj;
		*/
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);	// json 일 경우 
		httpHeaders.add("Access-Control-Allow-Origin", "*");			//
		httpHeaders.add("Access-Control-Allow-Headers", "*");
		httpHeaders.add("Access-Control-Allow-Credentials", "true");
		return new ResponseEntity<Map<String, Object>>(result, httpHeaders, HttpStatus.OK);
		
    }

    
}
