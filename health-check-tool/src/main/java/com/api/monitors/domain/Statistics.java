package com.api.monitors.domain;

import com.google.gson.JsonObject;

public class Statistics {
	
	private String name;
	private String url;
	private String tat;
	private boolean healthy;
	private JsonObject responseBody;
	
	public Statistics() {
	
	}
	
	public Statistics(String name, String url, boolean healthy, JsonObject responseBody, String tat){
		this.name = name;
		this.url = url;
		this.healthy = healthy;
		this.responseBody = responseBody;
		this.tat = tat;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTat() {
		return tat;
	}
	public void setTat(String tat) {
		this.tat = tat;
	}

	
	public boolean isHealthy() {
		return healthy;
	}

	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
	}

	public JsonObject getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(JsonObject responseBody) {
		this.responseBody = responseBody;
	}	

}