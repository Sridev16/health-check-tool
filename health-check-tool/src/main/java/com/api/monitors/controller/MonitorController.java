package com.api.monitors.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.monitors.domain.Statistics;
import com.api.monitors.service.MonitorService;

@RestController
public class MonitorController {
	
	@Autowired
	private MonitorService monImpl;
	
	@RequestMapping("/health/check/")
	public List<Statistics> healthCheck() {
		
		List<Statistics> resp = new ArrayList<>();
		
		try {
			resp = monImpl.getStats();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
}