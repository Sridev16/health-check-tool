package com.api.monitors.service;

import java.util.List;

import com.api.monitors.domain.ServiceDetails;

public interface ConfigFileService {
	
	List<ServiceDetails> readFile();
	boolean addService(ServiceDetails serviceDetail);
	boolean editService(String id, ServiceDetails serviceDetail);
	boolean removeService(String id);
	
}