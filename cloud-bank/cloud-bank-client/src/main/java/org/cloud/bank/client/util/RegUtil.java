package org.cloud.bank.client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正在表达式
 * @author admin
 *
 */
public class RegUtil {
	/**
	 * 匹配
	 * @param pattern 格式
	 * @param str
	 * @return
	 */
	public static boolean match(String reg,String str){
		Pattern p= Pattern.compile(reg);
		Matcher matcher= p.matcher(str);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	/**
	 * YN验证
	 * @param str
	 * @return
	 */
	public static boolean YN(String str){
		String pattern="^[YN]$";
		Pattern p= Pattern.compile(pattern);
		Matcher matcher= p.matcher(str);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	
	public static boolean date(String str){
		String pattern="^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
		Pattern p=Pattern.compile(pattern);
		Matcher matcher=p.matcher(str);
		if(matcher.find()){
			return true;
		}
		return false;
	}

}
