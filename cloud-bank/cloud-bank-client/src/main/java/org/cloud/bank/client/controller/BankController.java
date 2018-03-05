package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/bank")
public class BankController {
	
	private static final Logger logger = LoggerFactory.getLogger(BankController.class);
	
	@Autowired
	private BankService bankService;
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public Result<Object> list(@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Bank> banks=bankService.list(page,size);
		return new Result<Object>(200,null,banks);
	}
   
	@UserAuthor(level=0)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="name",required=true)String name){
		Bank bank=new Bank();
		bank.setName(name);
		bankService.add(bank);
		return new Result<Object>(200,null,null);
	}
	@UserAuthor(level=0)
	@RequestMapping(value="/del_banid",method=RequestMethod.POST)
	public Result<Object> del_banid(@RequestParam(value="banid",required=true)long banid){
		bankService.delByBanid(banid);
		return new Result<Object>(200,null,null);
	}
}
