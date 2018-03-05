package org.cloud.bank.client.exception;

public class IllegalDatumValueException extends Exception{

	
	/**
	 * 非法数据值异常
	 */
	private static final long serialVersionUID = 1119005200701898192L;

	public IllegalDatumValueException(){
		super();
	}
	
	public IllegalDatumValueException(String message){
		super(message);
	}
}
