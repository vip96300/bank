package org.cloud.bank.client.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.function.Supplier;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

public class FileUtils {
	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName){
		String suffix = null;
		try {
			suffix=fileName.substring(fileName.lastIndexOf("."));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suffix;
	}
	/**
	 * 压缩文件
	 * @param srcPathName 文件夹路径
	 * @param destFileName 压缩后的文件名称
	 */
    public static java.io.File zip(String srcPathName,String destFileName) {  
    	Zip zip = new Zip(); 
        zip.setBasedir(new java.io.File(srcPathName)); 
        java.io.File destFile=new java.io.File(destFileName);
        zip.setDestFile(destFile); 
        Project p = new Project();
        java.io.File srcFile=new java.io.File(srcPathName);
        p.setBaseDir(srcFile); 
        zip.setProject(p); 
        zip.execute(); 
        return destFile;
    }
    /**
     * 拷贝文件
     * @param source
     * @param target
     */
    public static void copy(File source,File target){
    	if(!source.exists()){
    		return;
    	}
		try {
			FileInputStream fis=new FileInputStream(source);
			FileOutputStream fos=new FileOutputStream(target);
			byte[] buffer=new byte[1024];
			int length=0;
			while((length=fis.read(buffer))!=-1){
				fos.write(buffer,0,length);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void test(){
    	
    }
}
