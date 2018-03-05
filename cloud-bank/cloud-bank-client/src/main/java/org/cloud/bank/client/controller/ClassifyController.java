package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Classify;
import org.cloud.bank.client.service.ClassifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/business/classify")
public class ClassifyController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassifyController.class);
	
	@Autowired
	private ClassifyService classifyService;
	@UserAuthor(level=0)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="name",required=true)String name,
			@RequestParam(value="step",required=true)int step,
			@RequestParam(value="isrequired",required=true)int isrequired){
		Classify classify=new Classify();
		classify.setName(name);
		if(step<0||step>3){
			return new Result<Object>(500,"step not valid",null);
		}
		classify.setStep(step);
		classify.setIsrequired(isrequired);
		classifyService.add(classify);
		return new Result<Object>(200,null,null);
	}
	@UserAuthor(level=0)
	@RequestMapping(value="/upd_claid",method=RequestMethod.POST)
	public Result<Object> upd_claid(@RequestParam(value="claid",required=true)long claid,
			@RequestParam(value="name",required=true)String name,
			@RequestParam(value="step",required=true)int step,
			@RequestParam(value="isrequired",required=true)int isrequired){
		Classify classify=new Classify();
		classify.setClaid(claid);
		classify.setName(name);
		if(step<0||step>3){
			return new Result<Object>(500,"step not valid",null);
		}
		classify.setStep(step);
		classify.setIsrequired(isrequired);
		classifyService.updByClaid(classify);
		return new Result<Object>(200,null,null);
	}
	
	/**
	 * 隐藏或显示
	 * @param claid
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/upd_isdel_claid",method=RequestMethod.POST)
	public Result<Object> upd_isdel_claid(@RequestParam(value="claid",required=true)long claid){
		classifyService.updIsdelByClaid(claid);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public Result<List> list(){
		List<Classify> classifys=classifyService.list();
		return new Result<List>(200,null,classifys);
	}
	
	@RequestMapping(value="/list_step",method=RequestMethod.POST)
	public Result<List> list_step(@RequestParam(value="step",required=true)int step){
		List<Classify> classifys=classifyService.listByStep(step);
		return new Result<List>(200,null,classifys);
	}
	
}
