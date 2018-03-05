package org.cloud.bank.client.util;

import java.io.InputStreamReader;
import java.util.Properties;

public class ErrorinfoUtils {

	public static Properties properties = new Properties();
	public static final String URL="errorinfo.properties";
	static{
		try {
			properties.load(new InputStreamReader(ErrorinfoUtils.class.getClassLoader().getResourceAsStream(URL), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getError(String code){
		return properties.getProperty(code);
	}
	
	/*public static void main(String[] orgs){
		System.out.println(ErrorinfoUtils.getError("001"));
	}*/
}
