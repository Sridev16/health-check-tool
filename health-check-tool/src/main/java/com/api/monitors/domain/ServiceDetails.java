package com.api.monitors.domain;

import java.util.List;

public class ServiceDetails {
	
	private String id;
	private String name;
	private String url;
	private String httpMethod;
	private String headerCookie;
	private String body;
	private String authentication;
	private String params;
	private String authHeader;
	private List<ResponseAttribute> respAttr;
	private List<ServiceDetails> services;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getHeaderCookie() {
		return headerCookie;
	}
	public void setHeaderCookie(String headerCookie) {
		this.headerCookie = headerCookie;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAuthentication() {
		return authentication;
	}
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
		
	public String getAuthHeader() {
		return authHeader;
	}
	public void setAuthHeader(String authHeader) {
		this.authHeader = authHeader;
	}
	public List<ResponseAttribute> getRespAttr() {
		return respAttr;
	}
	public void setRespAttr(List<ResponseAttribute> respAttr) {
		this.respAttr = respAttr;
	}
	public List<ServiceDetails> getServices() {
		return services;
	}
	public void setServices(List<ServiceDetails> services) {
		this.services = services;
	}
	
	@Override
	public String toString() {
		return "ServiceDetails [Id: "+ id + "Name: "+ name + "URL: " + url + "httpMethod: " + httpMethod +
				"headerCookie: " + headerCookie + "services: " + services + "]";
	}
	
}