package com.api.monitors.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.monitors.domain.ServiceDetails;
import com.api.monitors.service.ConfigFileService;

@Service
public class ConfigFileImpl implements ConfigFileService {

	@Override
	public List<ServiceDetails> readFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addService(ServiceDetails serviceDetail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editService(String id, ServiceDetails serviceDetail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeService(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}