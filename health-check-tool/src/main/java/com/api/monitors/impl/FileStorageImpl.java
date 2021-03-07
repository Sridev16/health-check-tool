package com.api.monitors.impl;


import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.api.monitors.service.FileStorageService;

@Service
public class FileStorageImpl implements FileStorageService {

	private Path fileStorageLocation;
	
	@Autowired
	@Value("${app.file.upload.dir}")
	private String uploadDirPath;
	
	public String storeFile(MultipartFile file) throws FileUploadException {
		
		fileStorageLocation = Paths.get(uploadDirPath).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileSystemNotFoundException("Could not create the directory where the uploaded files will be stored.");
		}
		
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if(fileName.contains("..")) {
				throw new FileUploadException("Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileUploadException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName) throws FileNotFoundException, MalformedURLException {
		try {
			fileStorageLocation = Paths.get(uploadDirPath).toAbsolutePath().normalize();
			Path filePath = fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MalformedURLException("File not found " + fileName);
		}
	}

}