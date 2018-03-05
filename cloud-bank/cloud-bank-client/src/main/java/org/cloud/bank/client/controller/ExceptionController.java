package org.cloud.bank.client.controller;

import org.cloud.bank.client.dto.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class ExceptionController {

	//@ExceptionHandler(value=Exception.class)
	public Result<Object> exception(){
		return new Result<Object>(999,"system exception",null);
	}
}
