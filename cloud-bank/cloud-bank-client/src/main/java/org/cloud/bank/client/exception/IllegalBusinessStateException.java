package org.cloud.bank.client.exception;

public class IllegalBusinessStateException extends Exception{

	/**
	 * 业务状态非法异常
	 */
	private static final long serialVersionUID = -3983514184543506375L;
	
	public IllegalBusinessStateException(){
		super();
	}
	
	public IllegalBusinessStateException(String message){
		super(message);
	}
	
}
