package com.api.monitors.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.api.monitors.domain.Statistics;

public interface MonitorService {

	List<Statistics> getStats() throws IOException, ParseException;
	
}