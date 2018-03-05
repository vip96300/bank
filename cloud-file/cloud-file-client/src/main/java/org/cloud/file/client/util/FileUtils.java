package org.cloud.file.client.util;

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
}
