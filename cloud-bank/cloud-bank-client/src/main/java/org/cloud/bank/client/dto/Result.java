package org.cloud.bank.client.dto;

public class Result <T>{
	
	private Class<T> clazz;
	
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
