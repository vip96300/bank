package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.AttValue;
import org.cloud.bank.client.service.AttValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/business/datum/attvalue")
public class AttValueController {
	
	private static final Logger logger = LoggerFactory.getLogger(AttValueController.class);
	
	@Autowired
	private AttValueService attValueService;
	
	@RequestMapping(value="/list_namid",method=RequestMethod.POST)
	public Result<List> list_namid(@RequestParam(value="namid",required=true)long namid){
		List<AttValue> attValues=attValueService.listByNamid(namid);
		return new Result<List>(200,null,attValues);
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="namid",required=true)long namid,
			@RequestParam(value="value",required=true)String value,
			@RequestParam(value="depict",required=true)String depict){
		AttValue attValue=new AttValue();
		attValue.setNamid(namid);
		attValue.setValue(value);
		attValue.setDepict(depict);
		attValueService.add(attValue);
		return new Result<Object>(200,null,null);
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/del_valid",method=RequestMethod.POST)
	public Result<Object> del_valid(@RequestParam(value="valid",required=true)long valid){
		attValueService.delByValid(valid);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public Result<List> list(){
		List<AttValue> attValues=attValueService.list();
		return new Result<List>(200,null,attValues);
	}
	
	@RequestMapping(value="/list_fkvalue",method=RequestMethod.POST)
	public Result<List> list_fkvalue(@RequestParam(value="fkvalue",required=true)String fkvalue){
		List<AttValue> attValues=attValueService.listByFkvalue(fkvalue);
		return new Result<List>(200,null,attValues);
	}
}
