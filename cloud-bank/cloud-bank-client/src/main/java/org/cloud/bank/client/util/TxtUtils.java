package org.cloud.bank.client.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.cloud.bank.client.config.CharSet;

public class TxtUtils {

	public static void write(){
		
	}
	
	/**
	 * 读取txt文件
	 * @param filePath
	 */
	public static List<String> read(File file){
		List<String> lines=new ArrayList<String>();
		try {                 
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),CharSet.GBK);
			BufferedReader bufferedReader = new BufferedReader(read);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			read.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return lines;  
    }
	/**
	 * 追加行
	 * @param file
	 * @param oldStr
	 */
	public static void append(File file,String line) {
		try {                 
			FileOutputStream out = new FileOutputStream(file,true);
			try {
				out.write(line.getBytes(CharSet.GBK));
				out.write("\n".getBytes(CharSet.GBK));
			} catch (Exception e) {
				e.printStackTrace();
			}
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

}
