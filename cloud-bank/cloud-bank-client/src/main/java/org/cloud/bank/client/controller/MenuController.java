package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Menu;
import org.cloud.bank.client.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/business/datum/menu")
public class MenuController {
	
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired
	private MenuService menuService;

	@UserAuthor(level=-1)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="name",required=true)String name,
			@RequestParam(value="pid",required=true)long pid,
			@RequestParam(value="issku",required=true)int issku){
		Menu menu=null;
		if(pid!=0){//pid为0代表顶级菜单，不为0代表子菜单
			menu=menuService.getByMenid(pid);
			if(menu.getIssku().intValue()!=0){//如果父菜单是最小单元则不能添加子菜单
				return new Result<Object>(500,"the menu is stock keeping unit",null);
			}
		}
		menu=new Menu();
		menu.setName(name);
		menu.setPid(pid);
		menu.setIssku(issku);
		menuService.add(menu);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/list_pid",method=RequestMethod.POST)
	public Result<List> list_pid(@RequestParam(value="pid",required=true)long pid){
		List<Menu> menus=menuService.listByPid(0);
		return new Result<List>(200,null,menus);
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/upd",method=RequestMethod.POST)
	public Result<Object> upd_menid(@RequestParam(value="menid",required=true)long menid,
			@RequestParam(value="name",required=true)String name){
		Menu menu=new Menu();
		menu.setMenid(menid);
		menu.setName(name);
		menuService.upd(menu);
		return new Result<Object>(200,null,null);
	}
	
	@UserAuthor(level=-1)
	@RequestMapping(value="/del_menid",method=RequestMethod.POST)
	public Result<Object> del_menid(@RequestParam(value="menid",required=true)long menid){
		menuService.delByMenid(menid);
		return new Result<Object>(200,null,null);
	}
}
