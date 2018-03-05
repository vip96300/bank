package org.cloud.file.client.service;

import java.util.List;

import org.cloud.file.client.model.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public List<File> upload_batch(String code,String names,List<MultipartFile> multipartFiles);
	
	public File upload(String code,String name,MultipartFile multipartFiles);
	
	public String upload_leadout(List<MultipartFile> multipartFiles);
	
}
