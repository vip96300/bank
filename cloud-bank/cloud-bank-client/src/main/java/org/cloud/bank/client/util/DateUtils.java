package org.cloud.bank.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static Date timestampToDate(long timestamp){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d=format.format(timestamp);
		Date date=null;
		try {
			date=format.parse(d);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}
	
	/*public static void main(String[] orgs){
		System.out.println(DateUtils.timestampToDate(System.currentTimeMillis()));
	}*/
}
