package com.api.monitors.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.monitors.domain.ServiceDetails;
import com.api.monitors.domain.Statistics;
import com.api.monitors.service.ClientService;
import com.api.monitors.service.MonitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class MonitorImpl implements MonitorService {
	
	@Autowired
	private ClientService clientImpl;
	
	static final Logger log = LogManager.getLogger(MonitorImpl.class.getName());
	
	public List<Statistics> getStats() throws IOException, ParseException {
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		// Loads YAML file
		File file = new File(classLoader.getResource("services.yaml").getFile());
		
		// Response object definition
		List<Statistics> stats = new ArrayList<>();

		// Instantiating a new ObjectMapper as a YAMLFactory
		ObjectMapper om = new ObjectMapper(new YAMLFactory());

		// Mapping the service details from the YAML file to the Service class
		ServiceDetails services = om.readValue(file, ServiceDetails.class);

		// Iterate thru the services list and invoke them
		for(ServiceDetails service:services.getServices()) {
			long startTime = System.currentTimeMillis();
			Statistics resp = clientImpl.client(service);
			long endTime = System.currentTimeMillis();
			Statistics s = new Statistics(service.getName(), service.getUrl(), resp.isHealthy(), resp.getResponseBody()
					, (endTime - startTime) + " ms");
			stats.add(s);
		}
		for(Statistics s: stats)
			log.info("{} >>>> {} >>>> {} >>>> {}", s.getName(), s.getUrl(), s.isHealthy(), s.getTat());
		
		return stats;
	}

}