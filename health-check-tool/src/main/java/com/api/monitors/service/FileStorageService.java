package com.api.monitors.service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	
	String storeFile(MultipartFile file) throws FileUploadException;
	Resource loadFileAsResource(String fileName) throws FileNotFoundException, MalformedURLException;

}