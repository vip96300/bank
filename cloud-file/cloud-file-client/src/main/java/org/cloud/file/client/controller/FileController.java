package org.cloud.file.client.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.cloud.file.client.dto.Result;
import org.cloud.file.client.model.File;
import org.cloud.file.client.service.FileService;
import org.cloud.file.client.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping(value="/file")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value="/upload_batch",method=RequestMethod.POST)
	public Result<List> upload_batch(@RequestParam(value="code",required=true)String code,
			@RequestParam(value="names",required=true)String names,
		MultipartHttpServletRequest request){
		List<MultipartFile> multipartFiles=new ArrayList<MultipartFile>();
		for(Map.Entry<String, MultipartFile> fileMap:request.getMultiFileMap().toSingleValueMap().entrySet()){
			multipartFiles.add(fileMap.getValue());
		}
		List<File> files=fileService.upload_batch(code,names,multipartFiles);
		return new Result<List>(200,null,files);
	}
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public Result<List> upload(@RequestParam(value="code",required=true)String code,
			@RequestParam(value="name",required=true)String name,
		MultipartHttpServletRequest request){
		List<MultipartFile> multipartFiles=new ArrayList<MultipartFile>();
		for(Map.Entry<String, MultipartFile> fileMap:request.getMultiFileMap().toSingleValueMap().entrySet()){
			multipartFiles.add(fileMap.getValue());
		}
		File file=fileService.upload(code,name,multipartFiles.get(0));
		return new Result<List>(200,null,file);
	}
	/**
	 * 银行端到处txt临时文件存放
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upload_leadout",method=RequestMethod.POST)
	public Result<String> upload_leadout(MultipartHttpServletRequest request){
		List<MultipartFile> multipartFiles=new ArrayList<MultipartFile>();
		for(Map.Entry<String, MultipartFile> fileMap:request.getMultiFileMap().toSingleValueMap().entrySet()){
			multipartFiles.add(fileMap.getValue());
		}
		String url=fileService.upload_leadout(multipartFiles);
		return new Result<String>(200,null,url);
	}
	/**
	 * 下载文件，
	 * 压缩文件存在于编号下面
	 * @param code
	 * @param response
	 */
	@RequestMapping(value="/download_batch",method=RequestMethod.GET)
	public Result<Object> download_batch(@RequestParam(value="code",required=true)String code,
			HttpServletResponse response){
		String destFileName=java.io.File.listRoots()[0].getPath()+"file/"+code+"/"+code+".zip";
		String srcPathName=java.io.File.listRoots()[0].getPath()+"file/"+code;
		java.io.File srcFile=new java.io.File(destFileName);
		if(!srcFile.exists()){
			srcFile.mkdirs();
		}
	    java.io.File file=new java.io.File(destFileName);
	    file.delete();
	    file=FileUtils.zip(srcPathName,destFileName);
        response.setHeader("conent-type", "application/octet-stream");  
        response.setContentType("application/octet-stream");  
        response.setHeader("Content-Disposition", "attachment; filename="+file.getName());  
        OutputStream os;
		try {
			os = response.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);   
	        InputStream is = null;  
	        is = new FileInputStream(file);  
	        BufferedInputStream bis = new BufferedInputStream(is);  
	        int length = 0;  
	        byte[] temp = new byte[1 * 1024 * 10];  
	        while ((length = bis.read(temp)) != -1) {  
	            bos.write(temp, 0, length);  
	        }  
	        bos.flush();  
	        bis.close();  
	        bos.close();  
	        is.close();    
		} catch (IOException e) {
			e.printStackTrace();
		}  
        return new Result<Object>(200,null,null);
	}
}
