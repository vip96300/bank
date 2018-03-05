package org.cloud.file.client.dto;

import java.lang.reflect.ParameterizedType;

public class Result <T>{
	
	private Class<?> clazz;
	
	public Result(){
	    ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];  
	}
	
	public Result(int code,String depict,Object data){
		this.code=code;
		this.depict=depict;
		this.data=data;
	}

	private int code;
	private String depict;
	private Object data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDepict() {
		if(depict==null){
			depict="";
		}
		return depict;
	}
	public void setDepict(String depict) {
		this.depict = depict;
	}
	public Object getData() {
		if(data==null){
			data=clazz;
		}
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
