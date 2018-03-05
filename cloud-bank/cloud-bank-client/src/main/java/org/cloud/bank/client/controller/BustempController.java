package org.cloud.bank.client.controller;

import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Bustemp;
import org.cloud.bank.client.service.BustempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/business/bustemp")
public class BustempController {
	
	private static final Logger logger = LoggerFactory.getLogger(BustempController.class);
	
	@Autowired
	private BustempService bustempService;
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="temid",required=true)long temid,
			@RequestParam(value="code",required=true)String code,
			@RequestParam(value="urls",required=true)String urls){
		Bustemp bustemp=new Bustemp();
		bustemp.setTemid(temid);
		bustemp.setCode(code);
		bustemp.setUrls(urls);
		bustempService.add(bustemp);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/get_code",method=RequestMethod.POST)
	public Result<Object> get_code(@RequestParam(value="code",required=true)String code){
		Bustemp bustemp=bustempService.getByCode(code);
		return new Result<Object>(200,null,bustemp);
	}
	
}
