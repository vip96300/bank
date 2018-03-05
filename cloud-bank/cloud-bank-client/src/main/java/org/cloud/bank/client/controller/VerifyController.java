package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Verify;
import org.cloud.bank.client.service.VerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/bank/business/verify")
public class VerifyController {
	
	private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);
	
	@Autowired
	private VerifyService verifyService;
	
	@RequestMapping(value="/list_busid",method=RequestMethod.POST)
	public Result<List> list_busid(@RequestParam(value="busid",required=true)long busid){
		List<Verify> verifys=verifyService.listByBusid(busid);
		return new Result<List>(200,null,verifys);
	}
	

}
