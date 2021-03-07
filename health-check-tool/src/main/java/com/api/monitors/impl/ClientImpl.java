package com.api.monitors.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.monitors.domain.ServiceDetails;
import com.api.monitors.domain.Statistics;
import com.api.monitors.service.ClientService;
import com.api.monitors.util.Constants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ClientImpl implements ClientService {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	static final Logger log = LogManager.getLogger(ClientImpl.class.getName());
	
	@SuppressWarnings("deprecation")
	@Override
	public Statistics client(ServiceDetails service) throws JsonProcessingException, ParseException {
		
		Statistics resp = new Statistics();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList((MediaType.APPLICATION_JSON)));
		headers.setContentType(MediaType.APPLICATION_JSON);
		Optional<String> headerCookieOpt = Optional.ofNullable(service.getHeaderCookie());
		Optional<String> authHeaderOpt = Optional.ofNullable(service.getAuthHeader());
		if (headerCookieOpt.isPresent()) {			
			// Reading cookies from text file
			String cookieHeader = readCookieFile();
			log.info("cookie: {}",cookieHeader);	
			headers.set("Cookie", cookieHeader);
		}
		if (authHeaderOpt.isPresent()) {
			headers.set("Authorization", authHeaderOpt.get());
		}
		Optional<String> requestParamsOpt = Optional.ofNullable(service.getParams());
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(service.getUrl());
		Map<String, String> paramMap = new HashMap<>();
		ResponseEntity<String> respEntity = null;
		if (requestParamsOpt.isPresent()) {
			paramMap = constructReqParams(requestParamsOpt.get());
			if (service.getHttpMethod().equals("POST")) {
				log.info(paramMap);
				String reqBodyData = new ObjectMapper().writeValueAsString(paramMap);
				requestEntity = new HttpEntity<>(reqBodyData, headers);
			}
		}
		String url = builder.buildAndExpand(paramMap).toUriString();
		log.info("URL {}",url);
		try {
			respEntity = restTemplate.exchange(url, HttpMethod.valueOf(service.getHttpMethod()), requestEntity, String.class);
			JsonObject jsonObject = new JsonParser().parse(respEntity.getBody()).getAsJsonObject();
			log.info("Response text {}",jsonObject);
			if (service.getRespAttr() != null && !validateResponse(service, jsonObject)) {
				resp.setHealthy(false);
				resp.setResponseBody(jsonObject);
			}
			else if (respEntity.getStatusCodeValue() == 200) {
				resp.setHealthy(true);
				resp.setResponseBody(jsonObject);
			}
			else 
				resp.setHealthy(false);
		}
		catch (Exception e) {
			log.error("Exception in REST call: {}",e.getMessage());
			resp.setHealthy(false);
		}
		return resp;
	}

	@Override
	public boolean validateResponse(ServiceDetails service, JsonObject jsonObject) {
		for (int i=0; i < service.getRespAttr().size(); i++) {
			StringTokenizer st = new StringTokenizer(service.getRespAttr().get(i).getName(), ".");
			String respValue = null;
			
			if (st.countTokens() == 1)
				respValue = jsonObject.get(service.getRespAttr().get(i).getName()).getAsString();
			else if (st.countTokens() == 2)
				respValue = jsonObject.getAsJsonObject((String) st.nextElement()).get((String) st.nextElement()).getAsString();
			else if (st.countTokens() == 3)
				respValue = jsonObject.getAsJsonObject((String) st.nextElement()).getAsJsonObject((String) st.nextElement())
				.get((String) st.nextElement()).getAsString();
			log.info("Expected value of {} is {} \nReceived value of {} is {}",
					service.getRespAttr().get(i).getName(), service.getRespAttr().get(i).getValue(),
					service.getRespAttr().get(i).getName(), respValue);
			if (respValue!= null && !respValue.equals(service.getRespAttr().get(i).getValue()))			
				return false;
		}
		return true;
	}

	@Override
	public boolean writeCookieFile(List<String> cookies) {
		try (FileWriter writer = new FileWriter(Constants.COOKIE_FILE, false)){
			for (String cookie: cookies)
				writer.write(cookie);
			return true;
		} catch (IOException e) {
			log.error("Exception in writing to cookie txt file: {}",e.getMessage());
			return false;
		}
	}

	@Override
	public String readCookieFile() {
		String cookie = null; 
		StringBuilder cookieHeader = new StringBuilder(); 
		try(BufferedReader br = new BufferedReader(new FileReader(Constants.COOKIE_FILE))){
			while ((cookie = br.readLine()) != null)
				cookieHeader.append(cookie);
		}
		catch (IOException e) {
			log.error("Exception in reading cookie file: {}",e.getMessage());
		}
		return cookieHeader.toString();
	}
	
	@Override
	public Map<String, String> constructReqParams(String params) throws ParseException {
		Map<String, String> paramMap = new HashMap<>();
		StringTokenizer st = new StringTokenizer(params, "&");
		while (st.hasMoreElements()) {
			String actualElement = st.nextToken();
			StringTokenizer et = new StringTokenizer(actualElement, "=");
			if (et.countTokens() != 2) {
				throw new ParseException("Unexpected format", 0);
			}
			String key = et.nextToken();
			String value = et.nextToken();
			paramMap.put(key, value);
		}
		return paramMap;
	}
}