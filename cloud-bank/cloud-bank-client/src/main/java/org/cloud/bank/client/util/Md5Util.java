package org.cloud.bank.client.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * md5
 * @author user
 *
 */
public class Md5Util{
	
	public static String md5(String str){ 
		String reStr = null; 
		try { 
			MessageDigest md5 = MessageDigest.getInstance("MD5"); 
		    byte[] bytes = md5.digest(str.getBytes()); 
		    StringBuffer stringBuffer = new StringBuffer(); 
		    for (byte b : bytes){ 
		    	int bt = b&0xff; 
		        if (bt < 16){ 
		        	stringBuffer.append(0); 
		        }  
		        stringBuffer.append(Integer.toHexString(bt)); 
		    } 
		    reStr = stringBuffer.toString(); 
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace(); 
		} 
		return reStr; 
	 } 	 
	
	/*public static void main(String []orgs){
		System.out.println(Md5Util.md5("123456"));
	}*/
}