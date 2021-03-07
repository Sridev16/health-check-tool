package com.api.monitors.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.api.monitors.domain.ServiceDetails;
import com.api.monitors.domain.Statistics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

public interface ClientService {
	
	Statistics client(ServiceDetails service) throws JsonProcessingException, ParseException;
	boolean validateResponse(ServiceDetails service, JsonObject jsonObject);
	boolean writeCookieFile(List<String> cookies);
	String readCookieFile();
	Map<String, String> constructReqParams(String params) throws ParseException;
}