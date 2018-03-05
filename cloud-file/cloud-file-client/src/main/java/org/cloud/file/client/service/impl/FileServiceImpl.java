package org.cloud.file.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.cloud.file.client.model.File;
import org.cloud.file.client.service.FileService;
import org.cloud.file.client.util.FileUtils;
import org.cloud.file.client.util.PinyinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{
	
	private static final Logger log=LoggerFactory.getLogger(FileServiceImpl.class);
	
	private static final String FILE_PATH="file/";

	@Override
	public List<File> upload_batch(String code,String names,List<MultipartFile> multipartFiles) {
		//获取系统根路径
		String rootPath=java.io.File.listRoots()[0].getPath();
		List<File> files=new ArrayList<File>();
		log.info("names:{}",names);
		String[] nameList=names.split(",");
		for(MultipartFile multipartFile:multipartFiles){
			long claid=Long.valueOf(multipartFile.getOriginalFilename().split("_")[0]);
			String curName="0";
			for(int j=0;j<nameList.length;j++){
				String[] nameSplit=nameList[j].split("_");
				long claidByName=Long.valueOf(nameSplit[0]);
				if(claid==claidByName){
					curName=nameSplit[2];
					break;
				}
			}
			//将中文路径变为拼音
			curName=PinyinUtils.getPingYin(curName);
			//文件相对路径
			String relativePath=FILE_PATH+code+"/"+curName+"/";
			//验证文件夹是否存在
			this.mkdir(rootPath+relativePath);
			//目标文件
			java.io.File destFile=new java.io.File(rootPath+relativePath+multipartFile.getOriginalFilename());
			//上传
			try {
				log.info("file absolute path:{}",destFile.getAbsolutePath());
				multipartFile.transferTo(destFile);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			File file = new File();
			file.setClaid(claid);
			file.setType(curName);
			file.setCode(code);
			file.setName(destFile.getName());
			file.setDir(relativePath+destFile.getName());
			file.setFormat(FileUtils.getSuffix(destFile.getName()));
			file.setSize(destFile.getUsableSpace());
			file.setTime(System.currentTimeMillis());
			files.add(file);
		}
		return files;
	}
	
	@Override
	public File upload(String code, String name,MultipartFile multipartFile) {
		//获取系统根路径
		String rootPath=java.io.File.listRoots()[0].getPath();
		//文件相对路径
		String relativePath=FILE_PATH+code+"/"+PinyinUtils.getPingYin(name)+"/";
		//验证文件夹是否存在
		this.mkdir(rootPath+relativePath);
		//目标文件
		java.io.File destFile=new java.io.File(rootPath+relativePath+multipartFile.getOriginalFilename());
		//上传
		try {
			multipartFile.transferTo(destFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		File file = new File();
		file.setCode(code);
		file.setName(destFile.getName());
		file.setDir(relativePath+destFile.getName());
		file.setFormat(FileUtils.getSuffix(destFile.getName()));
		file.setSize(destFile.getUsableSpace());
		file.setTime(System.currentTimeMillis());
		return file;
	}
	/**
	 * 验证文件夹是否存在，不存在就创建
	 * @param path
	 */
	private void mkdir(String path){
		java.io.File fileDir = new java.io.File(path);
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
	}
	@Override
	public String upload_leadout(List<MultipartFile> multipartFiles) {
		String rootPath=java.io.File.listRoots()[0].getPath();
		long time=System.nanoTime();
		//文件相对路径
		String relativePath=FILE_PATH+time+"/";
		//验证文件夹是否存在
		this.mkdir(rootPath+relativePath);
		for(MultipartFile multipartFile:multipartFiles){
			//目标文件
			java.io.File destFile=new java.io.File(rootPath+relativePath+multipartFile.getOriginalFilename());
			//上传
			try {
				log.info("file absolute path:{}",destFile.getAbsolutePath());
				multipartFile.transferTo(destFile);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		String srcPathName=rootPath+relativePath;
		String destFileName=rootPath+relativePath+time+".zip";
		FileUtils.zip(srcPathName,destFileName);
		return relativePath+time+".zip";
	}
	
	
	/*public static void main(String[] args) {
		System.out.println(PinyinUtils.getPingYin("其他"));;
	}*/
}
