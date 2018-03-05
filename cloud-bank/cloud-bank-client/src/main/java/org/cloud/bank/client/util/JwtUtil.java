package org.cloud.bank.client.util;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	/**
	 * token有效时间/天
	 */
	private static final int TOKEN_EXPIRATION_DAY=7;
	
	/**
	 * token生成器
	 * @param headerParams 请求头参数列表
	 * @param claims 请求体参数列表
	 * @return token
	 */
	public static String tokenGenerator(Map<String,Object> headerParams,Map<String,Object> bodyParams) {
		DateTime dt = new DateTime();
		Date expiration = dt.plusDays(TOKEN_EXPIRATION_DAY).toDate();
		bodyParams.put("expiration",expiration);
		String token=Jwts.builder()
			.setHeaderParams(headerParams)
			.setClaims(bodyParams)
			.signWith(SignatureAlgorithm.HS256, DesUtil.DEFAULT_PASSWORD_CRYPT_KEY)
			.compact();
		return token;
	}
}
