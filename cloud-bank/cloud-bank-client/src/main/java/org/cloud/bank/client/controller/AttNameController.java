package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.Menu;
import org.cloud.bank.client.service.AttNameService;
import org.cloud.bank.client.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/business/datum/attname")
public class AttNameController {
	
	private static final Logger logger = LoggerFactory.getLogger(AttNameController.class);
	
	@Autowired
	private AttNameService attNameService;
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value="/list_menid",method=RequestMethod.POST)
	public Result<List> list_menid(@RequestParam(value="menid",required=true)long menid){
		List<AttName> attNames=attNameService.listByMenid(menid);
		return new Result<List>(200,null,attNames);
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="menid",required=true)long menid,
			@RequestParam(value="name",required=true)String name,
			@RequestParam(value="depict",required=true)String depict,
			@RequestParam(value="length",required=true)int length,
			@RequestParam(value="isrequired",required=true)int isrequired,
			@RequestParam(value="intype",required=true)int intype,
			@RequestParam(value="defvalue",required=true)String defvalue,
			@RequestParam(value="cover",required=true)String cover,
			@RequestParam(value="remark",required=true)String remark,
			@RequestParam(value="isget",required=true)int isget
			){
		Menu menu=menuService.getByMenid(menid);
		if(menu.getIssku()==0){
			return new Result<Object>(500,"the menu not stock keeping unit",null);
		}
		AttName attName=new AttName();
		attName.setMenid(menid);
		attName.setName(name);
		attName.setDepict(depict);
		attName.setLength(length);
		attName.setIsrequired(isrequired);
		attName.setIntype(intype);
		attName.setDefvalue(defvalue);
		attName.setCover(cover);
		attName.setRemark(remark);
		attName.setIsget(isget);
		attNameService.add(attName);
		return new Result<Object>(200,null,null);
		
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/upd",method=RequestMethod.POST)
	public Result<Object> upd(@RequestParam(value="namid",required=true)long namid,
			@RequestParam(value="name",required=true)String name,
			@RequestParam(value="depict",required=true)String depict,
			@RequestParam(value="length",required=true)int length,
			@RequestParam(value="isrequired",required=true)int isrequired,
			@RequestParam(value="intype",required=true)int intype,
			@RequestParam(value="defvalue",required=true)String defvalue,
			@RequestParam(value="cover",required=true)String cover,
			@RequestParam(value="remark",required=true)String remark,
			@RequestParam(value="isget",required=true)int isget){
		AttName attName=attNameService.getByNamid(namid);
		attName.setName(name);
		attName.setDepict(depict);
		attName.setLength(length);
		attName.setIsrequired(isrequired);
		attName.setIntype(intype);
		attName.setDefvalue(defvalue);
		attName.setCover(cover);
		attName.setRemark(remark);
		attName.setIsget(isget);
		attNameService.upd(attName);
		return new Result<Object>(200,null,null);
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/del_namid",method=RequestMethod.POST)
	public Result<Object> del_namid(@RequestParam(value="namid",required=true)long namid){
		attNameService.delByNamid(namid);
		return new Result<Object>(200,null,null);
	}
	
	/**
	 * 合作商批量导入时查询必传与面签需要传入的数据
	 * @return
	 */
	@RequestMapping(value="/list_isget",method=RequestMethod.POST)
	public Result<List> list_isget(){
		List<AttName> attnames=attNameService.listByIsget();
		return new Result<List>(200,null,attnames);
	}
}
